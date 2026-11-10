package com.sebastian.workorderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssignWorkOrderRequest(@NotNull @Positive Integer workerId) {
}
