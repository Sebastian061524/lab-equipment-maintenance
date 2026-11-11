package com.sebastian.workorderservice.model;


import java.util.Objects;

public class WorkOrder {
    private Integer id;
    private Integer deviceId;
    private Integer campusId;
    private Integer reporterId;
    private Integer workerId;
    private String location;
    private String faultDescription;
    private WorkOrderStatus status;

    public WorkOrder(Integer id, Integer deviceId, Integer campusId, Integer reporterId, String location, String faultDescription){
        this.id = id;
        this.deviceId = deviceId;
        this.campusId = Objects.requireNonNull(campusId, "工单校区不能为空");
        this.reporterId = reporterId;
        this.location = location;
        this.faultDescription = faultDescription;
        this.status = WorkOrderStatus.PENDING_ASSIGNMENT;
    }

    public synchronized void assign(Integer workerId){
        if(this.status == WorkOrderStatus.PENDING_ASSIGNMENT){
            this.workerId = workerId;
            this.status = WorkOrderStatus.PENDING_ACCEPTANCE;
        } else{
            throw new IllegalStateException();
        }
    }

    public Integer getCampusId(){
        return this.campusId;
    }

    public synchronized void take(Integer currentWorkerId){
        if(this.status != WorkOrderStatus.PENDING_ACCEPTANCE){
            throw new IllegalStateException();
        }
        if (currentWorkerId == null || !currentWorkerId.equals(this.workerId)) {
            throw new SecurityException("只有被指派的维修员才能接单");
        }
        this.status = WorkOrderStatus.PENDING_COMPLETION;
    }

    public synchronized void finish(Integer currentWorkerId){
        if(this.status != WorkOrderStatus.PENDING_COMPLETION){
            throw new IllegalStateException();
        }
        if (currentWorkerId == null || !currentWorkerId.equals(this.workerId)) {
            throw new SecurityException("只有被指派的维修员才能标记完工");
        }
        this.status = WorkOrderStatus.PENDING_INSPECTION;
    }

    public synchronized void pass(){
        if(this.status == WorkOrderStatus.PENDING_INSPECTION){
            this.status = WorkOrderStatus.COMPLETED;
        } else{
            throw new IllegalStateException();
        }
    }

    public synchronized boolean isAssignedTo(Integer currentWorkerId) {
        return currentWorkerId != null && currentWorkerId.equals(this.workerId);
    }

}
