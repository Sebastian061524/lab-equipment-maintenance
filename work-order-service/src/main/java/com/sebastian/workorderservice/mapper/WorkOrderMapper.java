package com.sebastian.workorderservice.mapper;

import com.sebastian.workorderservice.model.WorkOrder;
import com.sebastian.workorderservice.model.WorkOrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WorkOrderMapper {


    int insert(WorkOrder workOrder);

    WorkOrder findById(@Param("id") Integer id);

    int assign(@Param("id") Integer id,  @Param("workerId") Integer workerId);

    int updateStatus(
            @Param("id") Integer id,
            @Param("expectedStatus") WorkOrderStatus expectedStatus,
            @Param("nextStatus") WorkOrderStatus nextStatus);



}
