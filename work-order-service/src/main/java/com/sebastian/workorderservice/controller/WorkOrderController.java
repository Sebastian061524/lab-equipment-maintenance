package com.sebastian.workorderservice.controller;

import com.sebastian.workorderservice.client.SparePartsClient;
import com.sebastian.workorderservice.dto.*;
import com.sebastian.workorderservice.model.WorkOrderStatus;
import com.sebastian.workorderservice.service.WorkOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/work-orders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService, SparePartsClient sparePartsClient){
        this.workOrderService = workOrderService;
    }

    @PostMapping
    ResponseEntity<Map<String, Integer>> create(@Valid @RequestBody CreateWorkOrderRequest request){
        Integer id = workOrderService.create(request, 1, 1001);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("workOrderId", id));
    }

    @PostMapping("/{workOrderId}/assign")
    public ResponseEntity<Void> assign(@PathVariable("workOrderId") Integer workOrderId, @Valid @RequestBody AssignWorkOrderRequest request) {
        Map<Integer, Integer> campusByWorkerId = Map.of(2001, 1, 2002, 2);
        Integer workerCampusId = campusByWorkerId.get(request.workerId());
        if(workerCampusId == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
        workOrderService.assign(workOrderId, request.workerId(), 1, workerCampusId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{workOrderId}/take")
    public ResponseEntity<Void> take(@PathVariable("workOrderId") Integer workOrderId){
        workOrderService.take(workOrderId, 2001);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{workOrderId}/finish")
    public ResponseEntity<Void> finish(@PathVariable("workOrderId") Integer workOrderId){
        workOrderService.finish(workOrderId, 2001);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{workOrderId}/pass")
    public ResponseEntity<Void> pass(@PathVariable("workOrderId") Integer workOrderId){
        workOrderService.pass(workOrderId, 1, true);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{workOrderId}/parts/reservations")
    public ResponseEntity<ReservePartResponse> reservePart(@PathVariable("workOrderId") Integer workOrderId,
                                                           @Valid @RequestBody ReserveWorkOrderPartRequest request){
        Integer currentWorkerId = 2001;
        ReservePartResponse response = workOrderService.reservePart(
                workOrderId,
                currentWorkerId,
                request.requestId(),
                request.partId(),
                request.quantity());

        return ResponseEntity.ok(response);

    }

}
