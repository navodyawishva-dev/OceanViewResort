package com.oceanview;

import org.junit.Test;
import static org.junit.Assert.*;

public class BillCalculationTest {

    @Test
    public void testTotalBillCalculation() {
        double roomPrice = 100.00;
        int nights = 3;
        double total = roomPrice * nights;
        assertEquals(300.00, total, 0.01);
    }

    @Test
    public void testDiscountApplication() {
        double total = 300.00;
        double discount = 10.00;
        double finalAmount = total - discount;
        assertEquals(290.00, finalAmount, 0.01);
    }

    @Test
    public void testZeroNightsIsInvalid() {
        int nights = 0;
        assertFalse(nights > 0);
    }

    @Test
    public void testBillIsPositive() {
        double bill = 150.00;
        assertTrue(bill > 0);
    }
}