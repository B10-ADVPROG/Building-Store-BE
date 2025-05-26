package id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.controller;

import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.dto.CreatePaymentRequest;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.dto.EditPaymentDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private PaymentController paymentController;

    private Payment payment;
    private CreatePaymentRequest createRequest;
    private EditPaymentDTO editRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.defaultHeader(anyString(), anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        
        paymentController = new PaymentController(paymentService, webClientBuilder, "http://localhost:8080");
        
        payment = new Payment.Builder()
                .customerId("customer-123")
                .amount(100000)
                .paymentMethod("CASH")
                .status("LUNAS")
                .transactionId("trans-123")
                .build();
                
        createRequest = new CreatePaymentRequest("customer-123", 100000, "CASH", "LUNAS", "trans-123");
        editRequest = new EditPaymentDTO("CICILAN");
    }

    @Test
    void testCreatePayment() {
        when(paymentService.create(any(Payment.class))).thenReturn(payment);

        ResponseEntity<Object> response = paymentController.createPayment(createRequest, "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(paymentService).create(any(Payment.class));
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = Arrays.asList(payment);
        when(paymentService.findAll()).thenReturn(payments);

        ResponseEntity<Object> response = paymentController.getAllPayments("Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        verify(paymentService).findAll();
    }

    @Test
    void testGetPaymentDetail() {
        when(paymentService.findById("payment-1")).thenReturn(payment);

        ResponseEntity<Map<String, Object>> response = paymentController.getPaymentDetail("payment-1", "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(paymentService).findById("payment-1");
    }

    @Test
    void testUpdatePaymentStatus() {
        when(paymentService.updateStatus("payment-1", "CICILAN")).thenReturn(payment);

        ResponseEntity<Map<String, String>> response = paymentController.updatePaymentStatus("payment-1", editRequest, "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(paymentService).updateStatus("payment-1", "CICILAN");
    }

    @Test
    void testDeletePayment() {
        ResponseEntity<?> response = paymentController.deletePayment("payment-1", "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(paymentService).delete("payment-1");
    }
}