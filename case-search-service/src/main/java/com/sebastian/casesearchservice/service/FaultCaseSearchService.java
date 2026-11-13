package com.sebastian.casesearchservice.service;

import com.sebastian.casesearchservice.client.ElasticsearchClient;
import com.sebastian.casesearchservice.dto.CompletedWorkOrderDetail;
import com.sebastian.casesearchservice.dto.FaultCaseSearchResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaultCaseSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public FaultCaseSearchService(ElasticsearchClient elasticsearchClient){
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<CompletedWorkOrderDetail> getFaultCaseByKeyword(String keyword, Integer campusId){
        FaultCaseSearchResponse response = elasticsearchClient.searchByFaultDescription(keyword, campusId);
        List<FaultCaseSearchResponse.SearchHit> hits = response.hits().hits();
        return hits.stream()
                .map(FaultCaseSearchResponse.SearchHit::source)
                .toList();
    }

    public List<CompletedWorkOrderDetail> getFaultCaseByDeviceModel(String deviceModel, Integer campusId){
        FaultCaseSearchResponse response = elasticsearchClient.searchByDeviceModel(deviceModel, campusId);
        List<FaultCaseSearchResponse.SearchHit> hits = response.hits().hits();
        return hits.stream()
                .map(FaultCaseSearchResponse.SearchHit::source)
                .toList();
    }
}
