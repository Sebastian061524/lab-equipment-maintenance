package com.sebastian.workorderservice.dto;

public record ReservePartRequest(
        String requestId,
        int workOrderId,
        int campusId,
        int partId,
        int quantity
) {
}
