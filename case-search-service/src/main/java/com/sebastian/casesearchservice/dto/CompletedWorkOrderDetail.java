package com.sebastian.casesearchservice.dto;

public record CompletedWorkOrderDetail(
        Integer workOrderId,
        Integer campusId,
        Integer deviceId,
        String deviceModel,
        String faultDescription
) {
}
