package com.sebastian.casesearchservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FaultCaseSearchResponse(
    SearchHits hits
) {
    public record SearchHits(List<SearchHit> hits){

    }

    public record SearchHit(@JsonProperty("_source") CompletedWorkOrderDetail source){

    }
}
