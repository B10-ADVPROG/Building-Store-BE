package id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthorizationService;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.dto.CreatePaymentRequest;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.dto.EditPaymentDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "https://building-store-fe-production.up.railway.app"})
@Validated
public class PaymentController {

    private final PaymentService paymentService;
    private final AuthorizationService authorizationService;

    @Autowired
    public PaymentController(PaymentService paymentService, AuthorizationService authorizationService) {
        this.paymentService = paymentService;
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPayments(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorizedAsCashierOrAdmin(authHeader)) {
            return createUnauthorizedResponse();
        }
        List<Payment> payments = paymentService.findAll();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable String id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorizedAsCashierOrAdmin(authHeader)) {
            return createUnauthorizedResponse();
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
            return createUnauthorizedResponse();
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
            return createUnauthorizedResponse();
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
            return createUnauthorizedResponse();
        }
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerName}")
    public ResponseEntity<?> getPaymentsByCustomer(@PathVariable String customerName, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorizedAsCashierOrAdmin(authHeader)) {
            return createUnauthorizedResponse();
        }
        List<Payment> payments = paymentService.findByCustomerName(customerName);
        return ResponseEntity.ok(payments);
    }

    private boolean isAuthorizedAsCashierOrAdmin(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        
        String token = authHeader.substring(7);
        
        try {
            // Check admin authorization first
            if (authorizationService.authorizeAdmin(token)) {
                return true;
            }
            
            // Use the correct method name for cashier
            return authorizationService.authorizeKasir(token);
            
        } catch (Exception e) {
            System.err.println("Authorization error: " + e.getMessage());
            return false;
        }
    }

    private boolean isAuthorizedAsAdmin(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        
        String token = authHeader.substring(7);
        
        try {
            return authorizationService.authorizeAdmin(token);
        } catch (Exception e) {
            System.err.println("Admin authorization error: " + e.getMessage());
            return false;
        }
    }

    private ResponseEntity<Object> createUnauthorizedResponse() {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", HttpStatus.FORBIDDEN.value());
        errorBody.put("error", "Forbidden");
        errorBody.put("message", "Access denied. Cashier or Administrator role required for payment operations.");
        errorBody.put("path", "/api/payments");
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody);
    }
}