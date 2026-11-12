package com.sebastian.workorderservice.mapper;

import com.sebastian.workorderservice.model.OutboxEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OutBoxEventMapper {

    int insertWorkOrderCompleted(@Param("workOrderId") Integer workOrderId);

    List<OutboxEvent> findPending(@Param("limit") int limit);

    int markSent(@Param("id") Long id);
}
