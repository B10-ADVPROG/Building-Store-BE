package id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.dto.AuthorizationRequest;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.dto.CreatePaymentRequest;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.dto.EditPaymentDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private WebClient.Builder webClientBuilder;
    
    @Mock
    private WebClient webClient;

    private PaymentController paymentController;

    private Payment payment;
    private CreatePaymentRequest createRequest;
    private EditPaymentDTO editRequest;
    
    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup WebClient builder
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.defaultHeader(anyString(), anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        
        // Setup simplified WebClient mock chain using generic mocking
        WebClient.RequestBodyUriSpec requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        // Setup response
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Authorized as administrator");
        ResponseEntity<Map<String, Object>> responseEntity = 
            new ResponseEntity<>(responseBody, HttpStatus.OK);
            
        Mono<ResponseEntity<Map<String, Object>>> mono = Mono.just(responseEntity);
        when(responseSpec.toEntity(any(ParameterizedTypeReference.class))).thenReturn(mono);
        
        // Create controller and disable auth for testing
        paymentController = new PaymentController(paymentService, webClientBuilder, "http://localhost:8080");
        ReflectionTestUtils.setField(paymentController, "authEnabled", false);
        
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

        ResponseEntity<Object> response = paymentController.createPayment(createRequest, "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(paymentService).create(any(Payment.class));
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = Arrays.asList(payment);
        when(paymentService.findAll()).thenReturn(payments);

        ResponseEntity<Object> response = paymentController.getAllPayments("Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(paymentService).findAll();
    }

    @Test
    void testGetPaymentDetail() {
        when(paymentService.findById("payment-1")).thenReturn(payment);

        ResponseEntity<Map<String, Object>> response = paymentController.getPaymentDetail("payment-1", "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(paymentService).findById("payment-1");
    }

    @Test
    void testUpdatePaymentStatus() {
        when(paymentService.findById("payment-1")).thenReturn(payment);
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