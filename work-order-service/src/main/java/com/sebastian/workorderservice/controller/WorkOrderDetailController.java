package com.sebastian.workorderservice.controller;

import com.sebastian.workorderservice.dto.CompletedWorkOrderDetail;
import com.sebastian.workorderservice.service.WorkOrderService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/work-orders")
public class WorkOrderDetailController {

    private final WorkOrderService workOrderService;

    public WorkOrderDetailController (WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @GetMapping("/{workOrderId}/completed-detail")
    public CompletedWorkOrderDetail findCompletedDetailById(@PathVariable Integer workOrderId){
        CompletedWorkOrderDetail detail = workOrderService.findCompletedDetailById(workOrderId);
        return detail;
    }

    @GetMapping("/completed-details")
    public List<CompletedWorkOrderDetail> findCompletedDetailsAfterId(
            @RequestParam(name = "afterId", defaultValue = "0") Integer afterId,
            @RequestParam(name = "limit", defaultValue = "20")Integer limit){
        return workOrderService.findCompletedDetailsAfterId(afterId, limit);
    }

}
