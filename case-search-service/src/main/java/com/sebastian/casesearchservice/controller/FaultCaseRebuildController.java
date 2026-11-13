package com.sebastian.casesearchservice.controller;

import com.sebastian.casesearchservice.service.FaultCaseRebuildService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class FaultCaseRebuildController {

    private final FaultCaseRebuildService faultCaseRebuildService;

    public FaultCaseRebuildController(FaultCaseRebuildService faultCaseRebuildService){
        this.faultCaseRebuildService = faultCaseRebuildService;
    }

    @PostMapping("/internal/fault-cases/rebuild")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rebuild(){
        faultCaseRebuildService.rebuild();
    }
}
