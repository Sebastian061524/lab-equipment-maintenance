package com.sebastian.workorderservice.dto;

public record ReservePartResponse(
        String requestId,
        int workOrderId,
        int campusId,
        int partId,
        int quantity,
        String status
) {
}
