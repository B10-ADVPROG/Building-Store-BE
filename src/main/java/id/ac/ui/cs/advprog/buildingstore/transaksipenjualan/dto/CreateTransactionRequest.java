package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.Map;

public class CreateTransactionRequest {

    @NotEmpty(message = "Product map must not be empty")
    private Map<String, Integer> products;

    public CreateTransactionRequest() {}

    public CreateTransactionRequest(Map<String, Integer> products) {
        this.products = products;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<String, Integer> products) {
        this.products = products;
    }
}
