package com.sebastian.workorderservice.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
@FeignClient(name = "spare-parts-service")
public interface SparePartsClient {

    @GetMapping("/internal/parts/ping")
    String pong();
}
