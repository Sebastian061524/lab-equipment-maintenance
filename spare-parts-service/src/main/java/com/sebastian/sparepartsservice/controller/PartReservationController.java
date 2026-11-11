package com.sebastian.sparepartsservice.controller;

import com.sebastian.sparepartsservice.dto.ReservePartRequest;
import com.sebastian.sparepartsservice.model.PartReservation;
import com.sebastian.sparepartsservice.service.PartReservationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/parts/reservations")
public class PartReservationController {

    private final PartReservationService partReservationService;

    public PartReservationController(PartReservationService partReservationService){
        this.partReservationService = partReservationService;
    }

    @PostMapping
    public PartReservation reserve(@Valid @RequestBody ReservePartRequest request){
        String requestId = request.requestId();
        Integer campusId = request.campusId();
        Integer partId = request.partId();
        Integer quantity = request.quantity();
        Integer workOrderId = request.workOrderId();
        PartReservation reservation = partReservationService.reserve(requestId, workOrderId, campusId, partId, quantity);
        return reservation;
    }


}
