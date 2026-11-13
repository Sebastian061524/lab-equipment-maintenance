package com.sebastian.workorderservice.mapper;

import com.sebastian.workorderservice.dto.CompletedWorkOrderDetail;
import com.sebastian.workorderservice.model.WorkOrder;
import com.sebastian.workorderservice.model.WorkOrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WorkOrderMapper {


    int insert(WorkOrder workOrder);

    WorkOrder findById(@Param("id") Integer id);

    int assign(@Param("id") Integer id,  @Param("workerId") Integer workerId);

    int updateStatus(
            @Param("id") Integer id,
            @Param("expectedStatus") WorkOrderStatus expectedStatus,
            @Param("nextStatus") WorkOrderStatus nextStatus);

    CompletedWorkOrderDetail findCompletedDetailById(@Param("workOrderId") Integer workOrderId);

    List<CompletedWorkOrderDetail> findCompletedDetailsAfterId(@Param("afterId") Integer afterId,
                                                               @Param("limit") Integer limit);

}
