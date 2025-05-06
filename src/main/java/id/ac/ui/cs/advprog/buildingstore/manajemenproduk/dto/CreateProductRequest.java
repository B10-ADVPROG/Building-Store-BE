package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    @NotNull(message = "Product name is required")
    private String productName;

    private String productDescription;

    @NotNull(message = "Product price is required")
    private Integer productPrice;

    private Integer productStock;

    public CreateProductRequest() {
    }

    public CreateProductRequest(String productName, String productDescription, Integer productPrice, Integer productStock) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productStock = productStock;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductStock() {
        return (productStock != null) ? productStock : 0;
    }


}
