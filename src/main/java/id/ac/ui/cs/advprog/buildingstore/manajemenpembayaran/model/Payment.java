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
    private String customerName;
    private int amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Payment() {
        // Auto-generate UUID for default constructor
        this.paymentId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Payment(Builder builder) {
        // Ensure paymentId is never null
        this.paymentId = builder.paymentId != null ? builder.paymentId : UUID.randomUUID().toString();
        setCustomerName(builder.customerName);
        setAmount(builder.amount);
        setPaymentMethod(builder.paymentMethod);
        setStatus(builder.status);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void setPaymentId(String paymentId) {
        if (paymentId == null || paymentId.isEmpty()) {
            throw new IllegalArgumentException("Payment id cannot be null or empty");
        }
        this.paymentId = paymentId;
    }

    public void setCustomerName(String customerName) {
        if (customerName == null || customerName.isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        this.customerName = customerName;
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

    public static class Builder {
        private String paymentId;
        private String customerName;
        private int amount;
        private String paymentMethod;
        private String status;

        private boolean customerNameRequired = true;
        private boolean amountRequired = true;
        private boolean paymentMethodRequired = true;
        private boolean statusRequired = true;

        public Builder() {
            this.paymentId = UUID.randomUUID().toString();
        }

        public Builder customerName(String customerName) {
            this.customerName = customerName;
            customerNameRequired = false;
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

        public Payment build() {
            if (customerNameRequired || amountRequired || paymentMethodRequired || statusRequired) {
                throw new IllegalStateException("Required fields are missing");
            }
            // Ensure paymentId is set before building
            if (this.paymentId == null) {
                this.paymentId = UUID.randomUUID().toString();
            }
            return new Payment(this);
        }
    }
}