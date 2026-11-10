package com.sebastian.workorderservice.model;

public enum WorkOrderStatus {
    PENDING_ASSIGNMENT, // 待派单
    PENDING_ACCEPTANCE, // 待接单
    PENDING_COMPLETION, // 待完成
    PENDING_INSPECTION, // 待验收
    COMPLETED,          // 已完成
}
