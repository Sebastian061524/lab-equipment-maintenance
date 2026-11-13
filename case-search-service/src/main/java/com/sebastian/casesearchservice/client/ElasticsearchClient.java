package com.sebastian.casesearchservice.client;

import com.sebastian.casesearchservice.dto.CompletedWorkOrderDetail;
import com.sebastian.casesearchservice.dto.FaultCaseSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.autoconfigure.RestClientSsl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class ElasticsearchClient {

    private final RestClient restClient;

    public ElasticsearchClient(RestClient.Builder builder, RestClientSsl ssl,
                               @Value("${elasticsearch.base-url}") String baseUrl,
                               @Value("${elasticsearch.username}") String userName,
                               @Value("${elasticsearch.password}") String password){
        this.restClient = builder.baseUrl(baseUrl)
                .apply(ssl.fromBundle("elasticsearch"))
                .defaultHeaders(headers -> headers.setBasicAuth(userName, password))
                .build();
    }

    public void save(CompletedWorkOrderDetail detail){
        restClient.put()
                .uri("/fault-cases/_doc/{id}", detail.workOrderId())
                .body(detail)
                .retrieve().toBodilessEntity();
    }

    public FaultCaseSearchResponse searchByFaultDescription(String keyword, Integer campusId){
        var descriptionMatch = Map.of(
                "match", Map.of("faultDescription", keyword)
        );

        var campusFilter = Map.of(
                "term", Map.of("campusId", Map.of("value", campusId))
        );

        var boolQuery = Map.of(
                "must", List.of(descriptionMatch),
                "filter", List.of(campusFilter)
        );

        var requestBody = Map.of("query", Map.of("bool", boolQuery));

        return restClient.post()
                .uri("/fault-cases/_search")
                .body(requestBody)
                .retrieve()
                .body(FaultCaseSearchResponse.class);
    }

    public FaultCaseSearchResponse searchByDeviceModel(String deviceModel, Integer campusId){

        var campusFilter = Map.of(
                "term",
                Map.of("campusId", Map.of("value", campusId))
        );
        var deviceModelFilter = Map.of(
                "term",
                Map.of("deviceModel", Map.of("value", deviceModel))
        );

        var boolQuery = Map.of(
                "filter", List.of(campusFilter, deviceModelFilter)
        );

        var requestBody = Map.of("query", Map.of("bool", boolQuery));

        return restClient.post()
                .uri("/fault-cases/_search")
                .body(requestBody)
                .retrieve()
                .body(FaultCaseSearchResponse.class);
    }


}
