package com.sebastian.workorderservice;

import com.sebastian.workorderservice.dto.CreateWorkOrderRequest;
import com.sebastian.workorderservice.service.WorkOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorkOrderServiceApplicationTests {

    @Autowired
    private WorkOrderService workOrderService;

    @Test
    void testWorkOrderService(){
        CreateWorkOrderRequest request = new CreateWorkOrderRequest(101, "实验楼 A302", "示波器无法开机");
        Integer id = workOrderService.create(request, 1, 1001);
        workOrderService.assign(id, 2001, 1,1);
        workOrderService.take(id, 2001);
        workOrderService.finish(id, 2001);
        ResponseStatusException noRole = assertThrows(ResponseStatusException.class, () -> workOrderService.pass(id, 1, false));
        assertEquals(403, noRole.getStatusCode().value());
        ResponseStatusException wrongCampus = assertThrows(
                ResponseStatusException.class, () -> workOrderService.pass(id, 2, true));
        assertEquals(403, wrongCampus.getStatusCode().value());
        assertDoesNotThrow(() -> workOrderService.pass(id, 1, true));

    }

}
