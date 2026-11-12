package com.sebastian.workorderservice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WorkOrderCompletionLogMapper {

     void insertIfAbsent(
            @Param("eventId") Long eventId,
            @Param("workOrderId") Integer workOrderId
    );
}
