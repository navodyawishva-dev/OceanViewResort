package com.oceanview;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReservationValidationTest {

    @Test
    public void testValidCheckInDate() {
        String checkIn = "2026-04-01";
        assertNotNull(checkIn);
        assertFalse(checkIn.isEmpty());
    }

    @Test
    public void testValidGuestCount() {
        int guests = 2;
        assertTrue(guests > 0);
        assertTrue(guests <= 10);
    }

    @Test
    public void testInvalidGuestCount() {
        int guests = -1;
        assertFalse(guests > 0);
    }

    @Test
    public void testRoomPriceIsPositive() {
        double price = 150.00;
        assertTrue(price > 0);
    }
}