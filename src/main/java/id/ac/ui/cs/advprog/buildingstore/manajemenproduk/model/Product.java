package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Product {
    private String productId;
    private String productName;
    private String productDescription;
    private int productPrice;
    private int productStock;

    public void setProductId(String productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        } else if (productId.isEmpty()) {
            throw new IllegalArgumentException("Product id cannot be empty");
        }
        this.productId = productId;
    }

    public void setProductName(String productName) {
        if (productName == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        } else if (productName.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        this.productName = productName;
    }

    public void setProductDescription(String productDescription) {
        if (productDescription == null) {
            throw new IllegalArgumentException("Product description cannot be null");
        } else if (productDescription.isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be empty");
        }
        this.productDescription = productDescription;
    }

    public void setProductPrice(int productPrice) {
        if (productPrice < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        this.productPrice = productPrice;
    }

    public void setProductStock(int productStock) {
        if (productStock < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
        this.productStock = productStock;
    }
}
