package com.sebastian.casesearchservice.service;

import com.sebastian.casesearchservice.client.ElasticsearchClient;
import com.sebastian.casesearchservice.client.WorkOrderClient;
import com.sebastian.casesearchservice.dto.CompletedWorkOrderDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaultCaseRebuildService {

    private final WorkOrderClient workOrderClient;
    private final ElasticsearchClient elasticsearchClient;

    public FaultCaseRebuildService(
            WorkOrderClient workOrderClient,
            ElasticsearchClient elasticsearchClient){
        this.workOrderClient = workOrderClient;
        this.elasticsearchClient = elasticsearchClient;
    }

    public void rebuild(){
        int afterId = 0;
        while(true){
            List<CompletedWorkOrderDetail> details = workOrderClient.findCompletedDetailsAfterId(afterId, 20);
            if(details.isEmpty()){
                return;
            }
            for (CompletedWorkOrderDetail detail : details) {
                elasticsearchClient.save(detail);
            }
            afterId = details.get(details.size() - 1).workOrderId();
        }
    }


}
