package id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class CreatePaymentRequest {
    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be non-negative")
    private Integer amount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    public CreatePaymentRequest(String customerId, Integer amount, String paymentMethod, String status, String transactionId) {
        this.customerId = customerId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.transactionId = transactionId;
    }

    // Getters
    public String getCustomerId() { return customerId; }
    public Integer getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
    public String getTransactionId() { return transactionId; }
}