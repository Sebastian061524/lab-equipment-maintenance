package com.sebastian.sparepartsservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
public class PingController {

    @GetMapping("/parts/ping")
    public String pong() {
        return "SPARE-PARTS-PONG";
    }

}
