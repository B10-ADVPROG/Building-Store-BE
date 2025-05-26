package id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.dto.AuthorizationRequest;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.dto.CreatePaymentRequest;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.dto.EditPaymentDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "https://building-store-fe-production.up.railway.app"})
@Validated
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    private final WebClient authWebClient;

    @Value("${auth.enabled:true}")
    private boolean authEnabled;

    public PaymentController(PaymentService paymentService,
                           WebClient.Builder webClientBuilder,
                           @Value("${auth.service.base-url:http://localhost:8080}") String authServiceBaseUrl) {
        this.paymentService = paymentService;
        this.authWebClient = webClientBuilder
                .baseUrl(authServiceBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @GetMapping
    public ResponseEntity<?> getAllPayments(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorizedAsCashierOrAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
        }
        List<Payment> payments = paymentService.findAll();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable String id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorizedAsCashierOrAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
        }
        Payment payment = paymentService.findById(id);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payment);
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@Valid @RequestBody CreatePaymentRequest requestBody, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorizedAsCashierOrAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
        }
        Payment newPayment = new Payment.Builder()
                .customerName(requestBody.getCustomerName())
                .amount(requestBody.getAmount())
                .paymentMethod(requestBody.getPaymentMethod())
                .status(requestBody.getStatus())
                .build();
        Payment savedPayment = paymentService.create(newPayment);
        return ResponseEntity.ok(savedPayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable String id, @RequestBody EditPaymentDTO requestBody, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorizedAsCashierOrAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
        }
        Payment existingPayment = paymentService.findById(id);
        if (existingPayment == null) {
            return ResponseEntity.notFound().build();
        }
        paymentService.updateStatus(id, requestBody.getStatus());
        return ResponseEntity.ok(Map.of("message", "Payment status updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable String id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorizedAsAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
        }
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerName}")
    public ResponseEntity<?> getPaymentsByCustomer(@PathVariable String customerName, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorizedAsCashierOrAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
        }
        List<Payment> payments = paymentService.findByCustomerName(customerName);
        return ResponseEntity.ok(payments);
    }

    private boolean isAuthorizedAsCashierOrAdmin(String authHeader) {
        if (!authEnabled) {
            return true;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        AuthorizationRequest requestBody = new AuthorizationRequest();
        requestBody.setToken(authHeader.substring(7));

        try {
            // Check admin authorization
            ResponseEntity<Map<String, Object>> adminResponse = authWebClient.post()
                    .uri("/auth/auth-admin/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block(Duration.ofSeconds(3));

            if (adminResponse != null && adminResponse.getStatusCode() == HttpStatus.OK &&
                adminResponse.getBody() != null && "Authorized as administrator".equals(adminResponse.getBody().get("message"))) {
                return true;
            }

            // Check cashier authorization
            ResponseEntity<Map<String, Object>> cashierResponse = authWebClient.post()
                    .uri("/auth/auth-kasir/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block(Duration.ofSeconds(3));

            return cashierResponse != null && cashierResponse.getStatusCode() == HttpStatus.OK &&
                   cashierResponse.getBody() != null && "Authorized as cashier".equals(cashierResponse.getBody().get("message"));

        } catch (Exception e) {
            System.err.println("Authorization error: " + e.getMessage());
            return false;
        }
    }

    private boolean isAuthorizedAsAdmin(String authHeader) {
        if (!authEnabled) {
            return true;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        AuthorizationRequest requestBody = new AuthorizationRequest();
        requestBody.setToken(authHeader.substring(7));

        try {
            ResponseEntity<Map<String, Object>> response = authWebClient.post()
                    .uri("/auth/auth-admin/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block(Duration.ofSeconds(3));

            return response != null &&
                    response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    "Authorized as administrator".equals(response.getBody().get("message"));
        } catch (Exception e) {
            System.err.println("Admin authorization error: " + e.getMessage());
            return false;
        }
    }
}