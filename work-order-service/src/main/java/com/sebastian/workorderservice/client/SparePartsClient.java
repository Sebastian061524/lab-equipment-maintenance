package com.sebastian.workorderservice.client;


import com.sebastian.workorderservice.dto.ReservePartRequest;
import com.sebastian.workorderservice.dto.ReservePartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "spare-parts-service")
public interface SparePartsClient {

    @GetMapping("/internal/parts/ping")
    String pong();

    @PostMapping("/internal/parts/reservations")
    ReservePartResponse reserve(@RequestBody ReservePartRequest request);
}
