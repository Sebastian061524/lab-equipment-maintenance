package com.sebastian.sparepartsservice.model;

public class PartReservation {

    private final String requestId;
    private final int workOrderId;
    private final int campusId;
    private final int partId;
    private final int quantity;
    private PartReservationStatus status;

    public PartReservation(String requestId, int workOrderId, int campusId, int partId, int quantity){
        this.requestId = requestId;
        this.workOrderId = workOrderId;
        this.campusId = campusId;
        this.partId = partId;
        this.quantity = quantity;
        this.status = PartReservationStatus.RESERVED;

    }

    public String getRequestId() {
        return requestId;
    }

    public int getWorkOrderId() {
        return workOrderId;
    }

    public int getCampusId() {
        return campusId;
    }

    public int getPartId() {
        return partId;
    }

    public PartReservationStatus getStatus() {
        return status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setStatus(PartReservationStatus status){
        this.status = status;
    }



}
