package com.sebastian.workorderservice;

import com.sebastian.workorderservice.dto.CompletedWorkOrderDetail;
import com.sebastian.workorderservice.dto.CreateWorkOrderRequest;
import com.sebastian.workorderservice.mapper.OutBoxEventMapper;
import com.sebastian.workorderservice.mapper.WorkOrderCompletionLogMapper;
import com.sebastian.workorderservice.mapper.WorkOrderMapper;
import com.sebastian.workorderservice.model.OutboxEvent;
import com.sebastian.workorderservice.model.WorkOrder;

import com.sebastian.workorderservice.model.WorkOrderStatus;
import com.sebastian.workorderservice.service.WorkOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WorkOrderMapperTests {

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Autowired
    private OutBoxEventMapper outBoxEventMapper;

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private WorkOrderCompletionLogMapper workOrderCompletionLogMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    void testInsertWorkOrderAndReturnGeneratedId(){
        WorkOrder workOrder = new WorkOrder(null, 101,1,1001,
                "实验楼 A302", "示波器无法开机");
        assertEquals(1, workOrderMapper.insert(workOrder));
        assertNotNull(workOrder.getId());

        WorkOrder saved = workOrderMapper.findById(workOrder.getId());
        assertNotNull(saved);
        assertEquals(workOrder.getId(), saved.getId());
        assertEquals(workOrder.getDeviceId(), saved.getDeviceId());
        assertEquals(workOrder.getCampusId(), saved.getCampusId());
    }

    @Test
    @Transactional
    void testAssign(){
        WorkOrder workOrder = new WorkOrder(null, 101,1,1001,
                "实验楼 A302", "示波器无法开机");
        assertEquals(1, workOrderMapper.insert(workOrder));
        Integer id = workOrder.getId();
        assertNotNull(id);
        assertEquals(1, workOrderMapper.assign(id, 2001));
        assertEquals(0, workOrderMapper.assign(id, 2002));

        WorkOrder saved = workOrderMapper.findById(id);
        assertTrue(saved.isAssignedTo(2001));
    }

    @Test
    @Transactional
    void testUpdateStatus(){
        WorkOrder workOrder = new WorkOrder(null, 101,1,1001,
                "实验楼 A302", "示波器无法开机");
        assertEquals(1, workOrderMapper.insert(workOrder));
        Integer id = workOrder.getId();
        assertNotNull(id);
        assertEquals(1, workOrderMapper.assign(id, 2001));
        assertEquals(0, workOrderMapper.updateStatus(id, WorkOrderStatus.PENDING_COMPLETION, WorkOrderStatus.PENDING_INSPECTION));
        assertEquals(1, workOrderMapper.updateStatus(id, WorkOrderStatus.PENDING_ACCEPTANCE, WorkOrderStatus.PENDING_COMPLETION));
        assertEquals(0, workOrderMapper.updateStatus(id, WorkOrderStatus.PENDING_ACCEPTANCE, WorkOrderStatus.PENDING_COMPLETION));
    }

    @Test
    @Transactional
    void testFindPendingAndMarkSent(){
        CreateWorkOrderRequest request = new CreateWorkOrderRequest(101, "实验楼 A302", "示波器无法开机");
        Integer id = workOrderService.create(request, 1, 1001);
        int count = outBoxEventMapper.insertWorkOrderCompleted(id);
        assertEquals(1, count);
        OutboxEvent event = outBoxEventMapper.findPending(20).stream()
                .filter(item -> id.equals(item.workOrderId())
                        && "WORK_ORDER_COMPLETED".equals(item.eventType()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("没找到工单 " + id + " 的待发送事件"));

        Long eventId = event.id();
        assertEquals(1, outBoxEventMapper.markSent(eventId));
        assertEquals(0, outBoxEventMapper.markSent(eventId));
        assertTrue(outBoxEventMapper.findPending(20).stream()
                .noneMatch(item -> eventId.equals(item.id())));

    }

    @Test
    @Transactional
    void testInsertCompletionLogTwice(){
        CreateWorkOrderRequest request = new CreateWorkOrderRequest(101, "实验楼 A302", "示波器无法开机");
        Integer id = workOrderService.create(request, 1, 1001);
        int count = outBoxEventMapper.insertWorkOrderCompleted(id);
        assertEquals(1, count);
        OutboxEvent event = outBoxEventMapper.findPending(20).stream()
                .filter(item -> id.equals(item.workOrderId())
                        && "WORK_ORDER_COMPLETED".equals(item.eventType()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("没找到工单 " + id + " 的待发送事件"));

        Long eventId = event.id();
        workOrderCompletionLogMapper.insertIfAbsent(eventId, event.workOrderId());
        workOrderCompletionLogMapper.insertIfAbsent(eventId, event.workOrderId());
        Integer sum = jdbcTemplate.queryForObject(
                "select count(*) from work_order_completion_log where event_id = ?",
                Integer.class,
                eventId
        );
        assertEquals(1, sum);
    }

    @Test
    @Transactional
    void testFindCompletedDetailById(){
        Integer deviceId = 900101;
        CreateWorkOrderRequest request = new CreateWorkOrderRequest(deviceId, "实验楼 A302", "示波器无法开机");
        Integer id = workOrderService.create(request, 1, 1001);
        jdbcTemplate.update("insert into device(id, model) values (?, ?)", deviceId, "X200");
        workOrderService.assign(id, 2001, 1, 1);
        workOrderService.take(id, 2001);
        workOrderService.finish(id, 2001);
        workOrderService.pass(id, 1, true);
        CompletedWorkOrderDetail detail = workOrderMapper.findCompletedDetailById(id);
        assertEquals(id, detail.workOrderId());
        assertEquals("X200", detail.deviceModel());
    }

}
