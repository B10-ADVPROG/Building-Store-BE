package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class SalesTransaction {

    public enum Status {
        IN_PROGRESS,
        COMPLETED
    }

    private String transactionId;
    private Map<Product, Integer> products;
    private Status status;
    private LocalDateTime timestamp;

    public SalesTransaction(Map<Product, Integer> product) {
        this.products = Collections.unmodifiableMap(new LinkedHashMap<>(product));
        this.status = Status.IN_PROGRESS;
        this.transactionId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public SalesTransaction() {
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
