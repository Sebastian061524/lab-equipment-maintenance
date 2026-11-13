package com.sebastian.casesearchservice.client;

import com.sebastian.casesearchservice.dto.CompletedWorkOrderDetail;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class WorkOrderClient {

    private final RestClient restClient;

    public WorkOrderClient(RestClient.Builder builder){
        builder.baseUrl("http://localhost:8080");
        RestClient client = builder.build();
        this.restClient = client;
    }

    public CompletedWorkOrderDetail findCompletedDetailById(Integer workOrderId){
        return restClient.get()
                .uri("/internal/work-orders/{workOrderId}/completed-detail", workOrderId)
                .retrieve().body(CompletedWorkOrderDetail.class);
    }

    public List<CompletedWorkOrderDetail> findCompletedDetailsAfterId(Integer afterId, Integer limit){
        return restClient.get()
                .uri("/internal/work-orders/completed-details?afterId={afterId}&limit={limit}", afterId, limit)
                .retrieve().body(new ParameterizedTypeReference<List<CompletedWorkOrderDetail>>(){});
    }

}
