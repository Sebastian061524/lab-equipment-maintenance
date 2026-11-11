package com.sebastian.sparepartsservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReservePartRequest(
        @NotBlank
        String requestId,
        @NotNull
        @Positive
        Integer workOrderId,
        @NotNull
        @Positive
        Integer campusId,
        @NotNull
        @Positive
        Integer partId,
        @NotNull
        @Positive
        Integer quantity
) {
}
