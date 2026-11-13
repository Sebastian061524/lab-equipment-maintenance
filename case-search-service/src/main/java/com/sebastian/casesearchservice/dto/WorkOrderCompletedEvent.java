package com.sebastian.casesearchservice.dto;

public record WorkOrderCompletedEvent(
        Long id,
        Integer workOrderId,
        String eventType
    ) {
}
