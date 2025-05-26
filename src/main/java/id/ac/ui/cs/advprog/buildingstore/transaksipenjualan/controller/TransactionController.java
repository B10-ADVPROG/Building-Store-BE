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
}
