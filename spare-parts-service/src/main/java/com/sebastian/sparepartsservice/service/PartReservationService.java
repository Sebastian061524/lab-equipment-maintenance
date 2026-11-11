package com.sebastian.sparepartsservice.service;

import com.sebastian.sparepartsservice.model.PartReservation;
import com.sebastian.sparepartsservice.model.PartReservationStatus;
import com.sebastian.sparepartsservice.model.PartStock;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
public class PartReservationService {

    private final Map<String, PartReservation> reservations = new HashMap<>();

    private final PartStock stock = new PartStock(1, 101, 10);

    public synchronized PartReservation reserve(
            String requestId, int workOrderId, int campusId, int partId, int quantity) {
        PartReservation existing = reservations.get(requestId);

        if (existing != null) {
            if (workOrderId != existing.getWorkOrderId()
                    || campusId != existing.getCampusId()
                    || partId != existing.getPartId()
                    || quantity != existing.getQuantity()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "同一个 requestId 对应的预留参数不一致");
            }
            return existing;
        }

        if (campusId != 1 || partId != 101) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到对应的备件库存");
        }

        stock.reserve(quantity);
        PartReservation reservation = new PartReservation(requestId, workOrderId, campusId, partId, quantity);
        reservations.put(requestId, reservation);
        return reservation;
    }

    public synchronized void release(String requestId){
        PartReservation partReservation = reservations.get(requestId);
        if(partReservation == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(partReservation.getStatus().equals(PartReservationStatus.RELEASED)){
            return;
        }
        if(partReservation.getStatus().equals(PartReservationStatus.ISSUED)){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        stock.release(partReservation.getQuantity());
        partReservation.setStatus(PartReservationStatus.RELEASED);
    }

    public synchronized void issue(String requestId){
        PartReservation partReservation = reservations.get(requestId);
        if(partReservation == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(partReservation.getStatus().equals(PartReservationStatus.ISSUED)){
            return;
        }
        if(partReservation.getStatus().equals(PartReservationStatus.RELEASED)){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        stock.issue(partReservation.getQuantity());
        partReservation.setStatus(PartReservationStatus.ISSUED);
    }

}
