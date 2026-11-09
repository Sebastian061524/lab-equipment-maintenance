package com.sebastian.workorderservice.controller;

import com.sebastian.workorderservice.client.SparePartsClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {

    private final SparePartsClient sparePartsClient;

    public PingController(SparePartsClient sparePartsClient){
        this.sparePartsClient = sparePartsClient;
    }

    @GetMapping
    public String pong(){
        return "PONG";
    }

    @GetMapping("/parts")
    public String pingParts() {
        return sparePartsClient.pong();
    }
}
