package com.sebastian.workorderservice;

import com.sebastian.workorderservice.dto.CreateWorkOrderRequest;
import com.sebastian.workorderservice.mapper.OutBoxEventMapper;
import com.sebastian.workorderservice.service.WorkOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WorkOrderRollbackTests {

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockitoBean(enforceOverride = true)
    private OutBoxEventMapper outBoxEventMapper;

    @Test
    void rollsBackWorkOrderWhenEventInsertFails(){
        CreateWorkOrderRequest request = new CreateWorkOrderRequest(101, "实验楼 A302", "验收回滚测试");
        Integer id = workOrderService.create(request, 1, 1001);
        String statusSql = "SELECT status FROM work_order WHERE id = ?";
        try {
            workOrderService.assign(id, 2001, 1,1);
            workOrderService.take(id, 2001);
            workOrderService.finish(id, 2001);

            IllegalStateException failure = new IllegalStateException("模拟时间写入失败");
            when(outBoxEventMapper.insertWorkOrderCompleted(id)).thenAnswer(invocation -> {
                assertEquals("COMPLETED", jdbcTemplate.queryForObject(statusSql, String.class, id));
                throw failure;
            });
            IllegalStateException thrown = assertThrows(
                    IllegalStateException.class, () -> workOrderService.pass(id, 1, true));
            assertSame(failure, thrown);
            assertEquals("PENDING_INSPECTION", jdbcTemplate.queryForObject(statusSql, String.class, id));
        } finally {
            jdbcTemplate.update("DELETE FROM work_order WHERE id = ?", id);
        }
    }

}
