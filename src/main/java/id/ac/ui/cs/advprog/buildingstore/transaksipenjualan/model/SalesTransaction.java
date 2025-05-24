package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

public class SalesTransaction {

    public enum Status {
        IN_PROGRESS,
        COMPLETED
    }

    @Getter
    private String transactionId;
    @Getter @Setter
    private Map<Product, Integer> products;
    @Getter @Setter
    private Status status;
    @Getter
    private LocalDateTime timestamp;

    public SalesTransaction(Map<Product, Integer> products) {
        this.products = products;
        this.status = Status.IN_PROGRESS;
        this.transactionId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public SalesTransaction() {
        this.products = new HashMap<>();
        this.status = Status.IN_PROGRESS;
        this.transactionId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }


    public void generateIdAndTimestamp() {
        this.transactionId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public int getTotalAmount() {
        return products.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getProductPrice() * entry.getValue())
                .sum();
    }
}
