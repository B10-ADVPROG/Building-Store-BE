package id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.service;

import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        payment = new Payment.Builder()
                .customerId("customer-123")
                .amount(100000)
                .paymentMethod("CASH")
                .status("LUNAS")
                .transactionId("trans-123")
                .build();
    }

    @Test
    void testCreatePayment() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.create(payment);

        assertNotNull(result);
        assertEquals(payment.getCustomerId(), result.getCustomerId());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testFindAllPayments() {
        List<Payment> payments = Arrays.asList(payment);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.findAll();

        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));
        verify(paymentRepository).findAll();
    }

    @Test
    void testFindPaymentById() {
        when(paymentRepository.findById("payment-1")).thenReturn(Optional.of(payment));

        Payment result = paymentService.findById("payment-1");

        assertEquals(payment, result);
        verify(paymentRepository).findById("payment-1");
    }

    @Test
    void testUpdatePaymentStatus() {
        when(paymentRepository.findById("payment-1")).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.updateStatus("payment-1", "CICILAN");

        assertEquals("CICILAN", result.getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testDeletePayment() {
        paymentService.delete("payment-1");
        verify(paymentRepository).deleteById("payment-1");
    }
}