package com.sebastian.casesearchservice.controller;

import com.sebastian.casesearchservice.dto.CompletedWorkOrderDetail;
import com.sebastian.casesearchservice.service.FaultCaseSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class FaultCaseSearchController {

    private final FaultCaseSearchService faultCaseSearchService;

    public FaultCaseSearchController(FaultCaseSearchService faultCaseSearchService){
        this.faultCaseSearchService = faultCaseSearchService;
    }

    @GetMapping("/fault-cases")
    public List<CompletedWorkOrderDetail> searchByKeyword(@RequestParam("keyword") String keyword){
        return faultCaseSearchService.getFaultCaseByKeyword(keyword, 1);
    }

    @GetMapping("/fault-cases/by-device-model")
    public List<CompletedWorkOrderDetail> searchByDeviceModel(@RequestParam("deviceModel") String deviceModel){
        return faultCaseSearchService.getFaultCaseByDeviceModel(deviceModel, 1);
    }


}
