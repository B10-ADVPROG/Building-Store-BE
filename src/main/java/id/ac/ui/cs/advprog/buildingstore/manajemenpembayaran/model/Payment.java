package id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    private String paymentId;
    private String customerId;
    private int amount;
    private String paymentMethod;
    private String status; // LUNAS or CICILAN
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Payment() {
    }

    public Payment(Builder builder) {
        setPaymentId(builder.paymentId);
        setCustomerId(builder.customerId);
        setAmount(builder.amount);
        setPaymentMethod(builder.paymentMethod);
        setStatus(builder.status);
        setTransactionId(builder.transactionId);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void setPaymentId(String paymentId) {
        if (paymentId == null || paymentId.isEmpty()) {
            throw new IllegalArgumentException("Payment id cannot be null or empty");
        }
        this.paymentId = paymentId;
    }

    public void setCustomerId(String customerId) {
        if (customerId == null || customerId.isEmpty()) {
            throw new IllegalArgumentException("Customer id cannot be null or empty");
        }
        this.customerId = customerId;
    }

    public void setAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
    }

    public void setPaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be null or empty");
        }
        this.paymentMethod = paymentMethod;
    }

    public void setStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        if (!status.equals("LUNAS") && !status.equals("CICILAN")) {
            throw new IllegalArgumentException("Status must be either LUNAS or CICILAN");
        }
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void setTransactionId(String transactionId) {
        if (transactionId == null || transactionId.isEmpty()) {
            throw new IllegalArgumentException("Transaction id cannot be null or empty");
        }
        this.transactionId = transactionId;
    }

    public static class Builder {
        private String paymentId;
        private String customerId;
        private int amount;
        private String paymentMethod;
        private String status;
        private String transactionId;

        private boolean customerIdRequired = true;
        private boolean amountRequired = true;
        private boolean paymentMethodRequired = true;
        private boolean statusRequired = true;
        private boolean transactionIdRequired = true;

        public Builder() {
            this.paymentId = UUID.randomUUID().toString();
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            customerIdRequired = false;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            amountRequired = false;
            return this;
        }

        public Builder paymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            paymentMethodRequired = false;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            statusRequired = false;
            return this;
        }

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            transactionIdRequired = false;
            return this;
        }

        public Payment build() {
            if (customerIdRequired || amountRequired || paymentMethodRequired || 
                statusRequired || transactionIdRequired) {
                throw new IllegalStateException("Required fields are missing");
            }
            return new Payment(this);
        }
    }
}