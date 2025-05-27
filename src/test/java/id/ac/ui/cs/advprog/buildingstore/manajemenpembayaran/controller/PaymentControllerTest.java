package id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthorizationService;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.dto.CreatePaymentRequest;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.dto.EditPaymentDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private AuthorizationService authorizationService;

    private PaymentController paymentController;

    private Payment payment;
    private CreatePaymentRequest createRequest;
    private EditPaymentDTO editRequest;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        paymentController = new PaymentController(paymentService, authorizationService);
        
        // Mock authorization to return true for tests
        when(authorizationService.authorizeAdmin(anyString())).thenReturn(true);
        when(authorizationService.authorizeKasir(anyString())).thenReturn(true);
        
        // Create test data
        payment = new Payment.Builder()
                .customerName("John Doe")
                .amount(100000)
                .paymentMethod("CASH")
                .status("LUNAS")
                .build();
                
        createRequest = new CreatePaymentRequest("John Doe", 100000, "CASH", "LUNAS");
        editRequest = new EditPaymentDTO("CICILAN");
    }

    @Test
    void testCreatePayment() {
        when(paymentService.create(any(Payment.class))).thenReturn(payment);

        ResponseEntity<?> response = paymentController.createPayment(createRequest, "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Payment);
        verify(paymentService).create(any(Payment.class));
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = Arrays.asList(payment);
        when(paymentService.findAll()).thenReturn(payments);

        ResponseEntity<?> response = paymentController.getAllPayments("Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        verify(paymentService).findAll();
    }

    @Test
    void testGetPaymentById() {
        when(paymentService.findById("payment-1")).thenReturn(payment);

        ResponseEntity<?> response = paymentController.getPaymentById("payment-1", "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Payment);
        verify(paymentService).findById("payment-1");
    }

    @Test
    void testGetPaymentById_NotFound() {
        when(paymentService.findById("payment-1")).thenReturn(null);

        ResponseEntity<?> response = paymentController.getPaymentById("payment-1", "Bearer token");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(paymentService).findById("payment-1");
    }

    @Test
    void testUpdatePayment() {
        when(paymentService.findById("payment-1")).thenReturn(payment);
        when(paymentService.updateStatus("payment-1", "CICILAN")).thenReturn(payment);

        ResponseEntity<?> response = paymentController.updatePayment("payment-1", editRequest, "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        verify(paymentService).findById("payment-1");
        verify(paymentService).updateStatus("payment-1", "CICILAN");
    }

    @Test
    void testUpdatePayment_NotFound() {
        when(paymentService.findById("payment-1")).thenReturn(null);

        ResponseEntity<?> response = paymentController.updatePayment("payment-1", editRequest, "Bearer token");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(paymentService).findById("payment-1");
        verify(paymentService, never()).updateStatus(anyString(), anyString());
    }

    @Test
    void testDeletePayment() {
        ResponseEntity<?> response = paymentController.deletePayment("payment-1", "Bearer token");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(paymentService).delete("payment-1");
    }

    @Test
    void testGetPaymentsByCustomer() {
        List<Payment> payments = Arrays.asList(payment);
        when(paymentService.findByCustomerName("John Doe")).thenReturn(payments);

        ResponseEntity<?> response = paymentController.getPaymentsByCustomer("John Doe", "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        verify(paymentService).findByCustomerName("John Doe");
    }

    @Test
    void testCreatePayment_Unauthorized() {
        when(authorizationService.authorizeAdmin(anyString())).thenReturn(false);
        when(authorizationService.authorizeKasir(anyString())).thenReturn(false);

        ResponseEntity<?> response = paymentController.createPayment(createRequest, "Bearer invalidtoken");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(paymentService, never()).create(any(Payment.class));
    }

    @Test
    void testGetAllPayments_Unauthorized() {
        when(authorizationService.authorizeAdmin(anyString())).thenReturn(false);
        when(authorizationService.authorizeKasir(anyString())).thenReturn(false);

        ResponseEntity<?> response = paymentController.getAllPayments("Bearer invalidtoken");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(paymentService, never()).findAll();
    }

    @Test
    void testGetPaymentById_Unauthorized() {
        when(authorizationService.authorizeAdmin(anyString())).thenReturn(false);
        when(authorizationService.authorizeKasir(anyString())).thenReturn(false);

        ResponseEntity<?> response = paymentController.getPaymentById("payment-1", "Bearer invalidtoken");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(paymentService, never()).findById(anyString());
    }

    @Test
    void testUpdatePayment_Unauthorized() {
        when(authorizationService.authorizeAdmin(anyString())).thenReturn(false);
        when(authorizationService.authorizeKasir(anyString())).thenReturn(false);

        ResponseEntity<?> response = paymentController.updatePayment("payment-1", editRequest, "Bearer invalidtoken");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(paymentService, never()).findById(anyString());
        verify(paymentService, never()).updateStatus(anyString(), anyString());
    }

    @Test
    void testDeletePayment_Unauthorized() {
        when(authorizationService.authorizeAdmin(anyString())).thenReturn(false);

        ResponseEntity<?> response = paymentController.deletePayment("payment-1", "Bearer invalidtoken");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(paymentService, never()).delete(anyString());
    }

    @Test
    void testCreatePayment_NoAuthHeader() {
        ResponseEntity<?> response = paymentController.createPayment(createRequest, null);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(paymentService, never()).create(any(Payment.class));
    }

    @Test
    void testCreatePayment_InvalidAuthHeader() {
        ResponseEntity<?> response = paymentController.createPayment(createRequest, "InvalidHeader");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(paymentService, never()).create(any(Payment.class));
    }
}