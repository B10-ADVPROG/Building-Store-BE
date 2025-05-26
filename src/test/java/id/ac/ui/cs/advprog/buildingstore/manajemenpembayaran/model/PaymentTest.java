package id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void testCreatePayment() {
        Payment payment = new Payment.Builder()
                .customerId("customer-123")
                .amount(100000)
                .paymentMethod("CASH")
                .status("LUNAS")
                .transactionId("trans-123")
                .build();

        assertNotNull(payment.getPaymentId());
        assertEquals("customer-123", payment.getCustomerId());
        assertEquals(100000, payment.getAmount());
        assertEquals("CASH", payment.getPaymentMethod());
        assertEquals("LUNAS", payment.getStatus());
        assertEquals("trans-123", payment.getTransactionId());
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
    void testSetNullCustomerId() {
        Payment payment = new Payment();
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setCustomerId(null);
        });
    }
}