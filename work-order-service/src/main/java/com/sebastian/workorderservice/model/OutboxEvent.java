package com.sebastian.workorderservice.model;

public record OutboxEvent(
        Long id,
        Integer workOrderId,
        String eventType
) {
}
