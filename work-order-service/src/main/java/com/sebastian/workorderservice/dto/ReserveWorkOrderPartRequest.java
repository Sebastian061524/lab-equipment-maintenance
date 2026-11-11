package com.sebastian.workorderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReserveWorkOrderPartRequest(
        @NotBlank String requestId,
        @NotNull @Positive Integer partId,
        @NotNull @Positive Integer quantity
) {}