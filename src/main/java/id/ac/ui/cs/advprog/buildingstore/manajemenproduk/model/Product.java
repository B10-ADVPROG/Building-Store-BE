package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;

import java.util.UUID;

@Getter
public class Product {
    private String productId;
    private String productName;
    private String productDescription;
    private int productPrice;
    private int productStock;

    public Product() {
    }

    public Product(Builder builder) {
        setProductId(builder.productId);
        setProductName(builder.productName);
        setProductDescription(builder.productDescription);
        setProductPrice(builder.productPrice);
        setProductStock(builder.productStock);
    }

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

    public static class Builder {
        private String productId;
        private String productName;
        private String productDescription;
        private int productPrice;
        private int productStock;

        private boolean nameRequired = true;
        private boolean priceRequired = true;

        public Builder() {  // Set the product id automatically and set default value for optional field
            this.productId = UUID.randomUUID().toString();
            this.productDescription = "";
            this.productStock = 0;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            nameRequired = false;
            return this;
        }

        public Builder productDescription(String productDescription) {
            this.productDescription = productDescription;
            return this;
        }

        public Builder productPrice(int productPrice) {
            this.productPrice = productPrice;
            priceRequired = false;
            return this;
        }

        public Builder productStock(int productStock) {
            this.productStock = productStock;
            return this;
        }

        public Product build() {
            if (nameRequired || priceRequired) {
                throw new IllegalStateException();
            }

            return new Product(this);
        }
    }


}
