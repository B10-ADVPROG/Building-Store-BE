package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.controller;

import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.dto.CreateTransactionRequest;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create/")
    public ResponseEntity<?> createTransaction(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @Valid @RequestBody CreateTransactionRequest request) {

        if (authorization == null || !authorization.equals("Bearer Token")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or missing token"));
        }

        transactionService.createRequest(request);

        return ResponseEntity.ok(Map.of("message", "Transaction is created successfully"));
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllTransactions(
            @RequestHeader(name = "Authorization", required = false) String authorization) {

        if (authorization == null || !authorization.equals("Bearer Token")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or missing token"));
        }

        return ResponseEntity.ok(transactionService.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTransactionStatus(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @PathVariable String id) {

        if (authorization == null || !authorization.equals("Bearer Token")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or missing token"));
        }

        try {
            transactionService.update(id);
            return ResponseEntity.ok(Map.of("message", "Transaction status updated successfully"));
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            if (msg.contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", msg));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", msg));
            }
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTransaction(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @PathVariable String id) {

        if (authorization == null || !authorization.equals("Bearer Token")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or missing token"));
        }

        try {
            transactionService.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Transaction deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }



}
