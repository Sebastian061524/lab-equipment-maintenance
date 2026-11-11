package com.sebastian.sparepartsservice;

import com.sebastian.sparepartsservice.model.PartReservation;
import com.sebastian.sparepartsservice.model.PartReservationStatus;
import com.sebastian.sparepartsservice.service.PartReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PartReservationServiceTests {


    @Test
    public void testReserve(){
        PartReservationService reservationService = new PartReservationService();
        reservationService.reserve("req-001", 1, 1, 101, 3);
        PartReservation reserve = reservationService.reserve("req-001", 1, 1, 101, 3);
        assertEquals(3, reserve.getQuantity());
        reservationService.reserve("req-002", 2, 1, 101, 7);
        ResponseStatusException conflict = assertThrows(ResponseStatusException.class, () -> reservationService.reserve("req-002", 2, 1, 101, 6));
        assertEquals(409, conflict.getStatusCode().value());
        assertThrows(IllegalStateException.class, () -> reservationService.reserve("req-003", 3, 1, 101, 1));
    }

    @Test
    public void testRelease(){
        PartReservationService reservationService = new PartReservationService();
        reservationService.reserve("req-001", 1, 1, 101, 3);
        reservationService.reserve("req-002", 2, 1, 101, 3);
        reservationService.release("req-001");
        reservationService.release("req-001");
        assertThrows(IllegalStateException.class, () -> reservationService.reserve("req-003", 3, 1, 101, 8));
        PartReservation remaining = reservationService.reserve("req-004", 3, 1, 101, 7);
        assertEquals(7, remaining.getQuantity());
    }

    @Test
    public void testIssue(){
        PartReservationService reservationService = new PartReservationService();
        PartReservation first = reservationService.reserve("req-001", 1, 1, 101, 3);
        PartReservation second = reservationService.reserve("req-002", 2, 1, 101, 3);
        reservationService.issue("req-001");
        reservationService.issue("req-001");
        reservationService.issue("req-002");
        assertEquals(PartReservationStatus.ISSUED, first.getStatus());
        assertEquals(PartReservationStatus.ISSUED, second.getStatus());
    }


}
