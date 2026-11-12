package com.sebastian.workorderservice.consumer;

import com.sebastian.workorderservice.mapper.WorkOrderCompletionLogMapper;
import com.sebastian.workorderservice.model.OutboxEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderCompletionListener {

    private final WorkOrderCompletionLogMapper workOrderCompletionLogMapper;

    public WorkOrderCompletionListener(WorkOrderCompletionLogMapper workOrderCompletionLogMapper){
        this.workOrderCompletionLogMapper = workOrderCompletionLogMapper;
    }

    @RabbitListener(
            queues = "work-order.completed",
            autoStartup = "${outbox.consumer.enabled:false}"
    )
    public void onWorkOrderCompleted(OutboxEvent event){
        workOrderCompletionLogMapper.insertIfAbsent(event.id(), event.workOrderId());
    }
}
