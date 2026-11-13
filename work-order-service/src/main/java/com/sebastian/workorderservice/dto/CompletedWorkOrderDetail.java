package com.sebastian.workorderservice.dto;

public record CompletedWorkOrderDetail(
        Integer workOrderId,
        Integer campusId,
        Integer deviceId,
        String deviceModel,
        String faultDescription
) {
}
