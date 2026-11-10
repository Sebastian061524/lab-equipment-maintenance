package com.sebastian.workorderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateWorkOrderRequest(
        @NotNull
        @Positive
        Integer deviceId,
        @NotBlank
        String location,
        @NotBlank
        String faultDescription
) {

}
