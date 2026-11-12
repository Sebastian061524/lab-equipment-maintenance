package com.sebastian.workorderservice.service;

import com.sebastian.workorderservice.dto.CreateWorkOrderRequest;
import com.sebastian.workorderservice.mapper.OutBoxEventMapper;
import com.sebastian.workorderservice.mapper.WorkOrderMapper;
import com.sebastian.workorderservice.model.WorkOrder;
import com.sebastian.workorderservice.client.SparePartsClient;
import com.sebastian.workorderservice.dto.ReservePartRequest;
import com.sebastian.workorderservice.dto.ReservePartResponse;
import com.sebastian.workorderservice.model.WorkOrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
public class WorkOrderService {

    private final SparePartsClient sparePartsClient;
    private final WorkOrderMapper workOrderMapper;
    private final OutBoxEventMapper outBoxEventMapper;

    public WorkOrderService(SparePartsClient sparePartsClient, WorkOrderMapper workOrderMapper, OutBoxEventMapper outBoxEventMapper) {
        this.sparePartsClient = sparePartsClient;
        this.workOrderMapper = workOrderMapper;
        this.outBoxEventMapper = outBoxEventMapper;
    }

    public Integer create(CreateWorkOrderRequest request, Integer campusId, Integer reporterId){

        Integer deviceId = request.deviceId();
        String location = request.location();
        String faultDescription = request.faultDescription();
        WorkOrder workOrder = new WorkOrder(null,deviceId, campusId, reporterId, location, faultDescription);
        workOrderMapper.insert(workOrder);
        return workOrder.getId();
    }

    public void assign(Integer workOrderId, Integer workerId, Integer dispatcherCampusId, Integer workerCampusId){
        WorkOrder workOrder = workOrderMapper.findById(workOrderId);
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
        int assign = workOrderMapper.assign(workOrderId, workerId);
        if(assign == 0){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    public void take(Integer workOrderId, Integer currentWorkerId){
        WorkOrder workOrder = workOrderMapper.findById(workOrderId);
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
        int updatedRows = workOrderMapper.updateStatus(
                workOrderId,
                WorkOrderStatus.PENDING_ACCEPTANCE,
                WorkOrderStatus.PENDING_COMPLETION);
        if(updatedRows == 0){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    public void finish(Integer workOrderId, Integer currentWorkerId){
        WorkOrder workOrder = workOrderMapper.findById(workOrderId);
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
        int updatedRows = workOrderMapper.updateStatus(
                workOrderId,
                WorkOrderStatus.PENDING_COMPLETION,
                WorkOrderStatus.PENDING_INSPECTION);
        if(updatedRows == 0){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

    }

    @Transactional
    public void pass(Integer workOrderId, Integer inspectorCampusId, boolean hasInspectionRole){
        WorkOrder workOrder = workOrderMapper.findById(workOrderId);
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
        int updatedRows = workOrderMapper.updateStatus(
                workOrderId,
                WorkOrderStatus.PENDING_INSPECTION,
                WorkOrderStatus.COMPLETED);
        if(updatedRows == 0){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        int count = outBoxEventMapper.insertWorkOrderCompleted(workOrderId);
        if(count != 1){
            throw new IllegalStateException("验收完成事件写入失败");
        }
    }

    public ReservePartResponse reservePart(Integer workOrderId, Integer currentWorkerId,
                                           String requestId, Integer partId, Integer quantity){

        WorkOrder workOrder = workOrderMapper.findById(workOrderId);
        if(workOrder == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "工单不存在");
        }
        if(!workOrder.isAssignedTo(currentWorkerId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "只有被指派的维修员才能预留备件");
        }
        ReservePartRequest request =
                new ReservePartRequest(requestId, workOrderId, workOrder.getCampusId(), partId, quantity);

        return sparePartsClient.reserve(request);
    }

}
