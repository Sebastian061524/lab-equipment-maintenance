package com.sebastian.casesearchservice.consumer;

import com.sebastian.casesearchservice.client.ElasticsearchClient;
import com.sebastian.casesearchservice.client.WorkOrderClient;
import com.sebastian.casesearchservice.dto.CompletedWorkOrderDetail;
import com.sebastian.casesearchservice.dto.WorkOrderCompletedEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;


import java.nio.charset.StandardCharsets;


@Component
public class WorkOrderCompletedListener {

    private final JsonMapper jsonMapper;
    private final WorkOrderClient workOrderClient;
    private final ElasticsearchClient elasticsearchClient;

    public WorkOrderCompletedListener(JsonMapper jsonMapper,
                                      WorkOrderClient workOrderClient,
                                      ElasticsearchClient elasticsearchClient) {
        this.jsonMapper = jsonMapper;
        this.workOrderClient = workOrderClient;
        this.elasticsearchClient = elasticsearchClient;
    }

    @RabbitListener(queues = "work-order.completed")
    public void onWorkOrderCompleted(Message message){
        String s = new String(message.getBody(), StandardCharsets.UTF_8);
        WorkOrderCompletedEvent event = jsonMapper.readValue(s, WorkOrderCompletedEvent.class);
        CompletedWorkOrderDetail detail = workOrderClient.findCompletedDetailById(event.workOrderId());
        elasticsearchClient.save(detail);
    }
}
