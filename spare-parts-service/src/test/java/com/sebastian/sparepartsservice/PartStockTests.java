package com.sebastian.sparepartsservice;

import com.sebastian.sparepartsservice.model.PartStock;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class PartStockTests {

    @Test
    void testReverse() {
        PartStock partStock = new PartStock(1, 1, 10);

        partStock.reserve(3);
        assertEquals(7, partStock.getAvailableQuantity());

        assertThrows(IllegalStateException.class, () -> partStock.reserve(8));
        assertEquals(7, partStock.getAvailableQuantity());

        assertThrows(IllegalArgumentException.class, () -> partStock.reserve(0));
        assertEquals(7, partStock.getAvailableQuantity());
    }

    @Test
    void testIssue(){
        PartStock partStock = new PartStock(1, 1, 10);
        partStock.reserve(3);
        partStock.issue(3);

        assertEquals(7, partStock.getAvailableQuantity());
        assertEquals(0, partStock.getReservedQuantity());
        assertEquals(7, partStock.getTotalQuantity());
    }

    @Test
    void testRelease(){
        PartStock partStock = new PartStock(1, 1, 10);
        partStock.reserve(3);
        partStock.release(3);
        assertEquals(10, partStock.getTotalQuantity());
        assertEquals(0, partStock.getReservedQuantity());
        assertEquals(10, partStock.getAvailableQuantity());
    }

}
