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
@RequestMapping("/payment")
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

    @GetMapping("/")
    public ResponseEntity<Object> getAllPayments(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorizedAsCashierOrAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or missing token"));
        }

        List<Payment> payments = paymentService.findAll();
        List<Map<String, Object>> response = new ArrayList<>();

        if (payments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No payments available"));
        }

        for (Payment payment : payments) {
            Map<String, Object> paymentMap = new HashMap<>();
            paymentMap.put("paymentId", payment.getPaymentId());
            paymentMap.put("customerName", payment.getCustomerName());
            paymentMap.put("amount", payment.getAmount());
            paymentMap.put("paymentMethod", payment.getPaymentMethod());
            paymentMap.put("status", payment.getStatus());
            paymentMap.put("createdAt", payment.getCreatedAt());
            paymentMap.put("updatedAt", payment.getUpdatedAt());
            response.add(paymentMap);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}/")
    public ResponseEntity<Map<String, Object>> getPaymentDetail(@PathVariable String id, @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorizedAsCashierOrAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or missing token"));
        }

        Payment payment = paymentService.findById(id);

        if (payment != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("paymentId", payment.getPaymentId());
            response.put("customerName", payment.getCustomerName());
            response.put("amount", payment.getAmount());
            response.put("paymentMethod", payment.getPaymentMethod());
            response.put("status", payment.getStatus());
            response.put("createdAt", payment.getCreatedAt());
            response.put("updatedAt", payment.getUpdatedAt());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPayment(@Valid @RequestBody CreatePaymentRequest requestBody, @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorizedAsCashierOrAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or missing token"));
        }

        try {
            Payment newPayment = new Payment.Builder()
                    .customerName(requestBody.getCustomerName())
                    .amount(requestBody.getAmount())
                    .paymentMethod(requestBody.getPaymentMethod())
                    .status(requestBody.getStatus())
                    .build();

            Payment savedPayment = paymentService.create(newPayment);

            // Return the created payment with its ID
            Map<String, Object> response = new HashMap<>();
            response.put("message", "New payment record created successfully");
            response.put("paymentId", savedPayment.getPaymentId());
            response.put("payment", savedPayment);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", "Invalid request: " + e.getMessage()));
        }
    }

    @PutMapping("/edit/{id}/")
    public ResponseEntity<Map<String, String>> updatePaymentStatus(@PathVariable("id") String id, @RequestBody EditPaymentDTO requestBody, @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorizedAsCashierOrAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or missing token"));
        }

        try {
            Payment existingPayment = paymentService.findById(id);
            if (existingPayment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Payment not found"));
            }

            paymentService.updateStatus(id, requestBody.getStatus());

            return ResponseEntity.ok(Map.of("message", "Payment status updated successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", "Failed to update payment: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}/")
    public ResponseEntity<?> deletePayment(@PathVariable String id, @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorizedAsAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }
        try {
            paymentService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Payment deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", "Failed to delete payment: " + e.getMessage()));
        }
    }

    @GetMapping("/customer/{customerName}/")
    public ResponseEntity<Object> getPaymentsByCustomer(@PathVariable String customerName, @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorizedAsCashierOrAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or missing token"));
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