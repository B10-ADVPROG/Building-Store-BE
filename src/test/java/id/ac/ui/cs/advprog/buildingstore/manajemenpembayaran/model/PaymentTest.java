package id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void testCreatePayment() {
        Payment payment = new Payment.Builder()
                .customerName("John Doe")
                .amount(100000)
                .paymentMethod("CASH")
                .status("LUNAS")
                .build();

        assertNotNull(payment.getPaymentId());
        assertEquals("John Doe", payment.getCustomerName());
        assertEquals(100000, payment.getAmount());
        assertEquals("CASH", payment.getPaymentMethod());
        assertEquals("LUNAS", payment.getStatus());
    }

    @Test
    void testPaymentBuilderRequiredFields() {
        assertThrows(IllegalStateException.class, () -> {
            new Payment.Builder().build();
        });
    }

    @Test
    void testSetInvalidAmount() {
        Payment payment = new Payment();
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setAmount(-1000);
        });
    }

    @Test
    void testSetNullCustomerName() {
        Payment payment = new Payment();
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setCustomerName(null);
        });
    }
}