package com.sebastian.workorderservice.service;

import com.sebastian.workorderservice.dto.CreateWorkOrderRequest;
import com.sebastian.workorderservice.model.WorkOrder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class WorkOrderService {

    private final AtomicInteger idGenerator = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer, WorkOrder> workOrders = new ConcurrentHashMap<>();

    public Integer create(CreateWorkOrderRequest request, Integer campusId, Integer reporterId){
        Integer id = idGenerator.incrementAndGet();
        Integer deviceId = request.deviceId();
        String location = request.location();
        String faultDescription = request.faultDescription();
        WorkOrder workOrder = new WorkOrder(id, deviceId, campusId, reporterId, location, faultDescription);
        workOrders.put(id, workOrder);
        return id;
    }

    public void assign(Integer workOrderId, Integer workerId, Integer dispatcherCampusId, Integer workerCampusId){
        WorkOrder workOrder = workOrders.get(workOrderId);
        if(workOrder == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "工单不存在, ID:" + workOrderId);
        }
        Integer campusId = workOrder.getCampusId();
        if(!campusId.equals(dispatcherCampusId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "工单校区与调度员所在校区不符");
        }
        if(!campusId.equals(workerCampusId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "维修员与工单不在同一校区");
        }
        try {
            workOrder.assign(workerId);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "只有待派单的工单才能派单");
        }
    }

    public void take(Integer workOrderId, Integer currentWorkerId){
        WorkOrder workOrder = workOrders.get(workOrderId);
        if(workOrder == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "工单不存在, ID:" + workOrderId);
        }
        try {
            workOrder.take(currentWorkerId);
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } catch (IllegalStateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    public void finish(Integer workOrderId, Integer currentWorkerId){
        WorkOrder workOrder = workOrders.get(workOrderId);
        if(workOrder == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "工单不存在, ID:" + workOrderId);
        }
        try {
            workOrder.finish(currentWorkerId);
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } catch (IllegalStateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    public void pass(Integer workOrderId, Integer inspectorCampusId, boolean hasInspectionRole){
        WorkOrder workOrder = workOrders.get(workOrderId);
        if(workOrder == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "工单不存在, ID:" + workOrderId);
        }
        boolean sameCampus = inspectorCampusId != null && inspectorCampusId.equals(workOrder.getCampusId());

        if (!hasInspectionRole || !sameCampus) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        try {
            workOrder.pass();
        } catch (IllegalStateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }


}
