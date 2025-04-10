package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product.Builder()
                .productName("Semen Tiga Roda 50 kg")
                .productDescription("Semen berkualitas tinggi untuk konstruksi bangunan")
                .productPrice(80000)
                .productStock(100)
                .build();
    }


    // ================ HAPPY PATH TESTS ================ //

    @Test
    void testGetProduct() {
        assertEquals("Semen Tiga Roda 50 kg", product.getProductName());
        assertEquals("Semen berkualitas tinggi untuk konstruksi bangunan", product.getProductDescription());
        assertEquals(80000, product.getProductPrice());
        assertEquals(100, product.getProductStock());

    }

    @Test
    void testSetProduct() {
        String productId = "d255e6f5-448d-42b9-9f77-287082e5ab80";
        String productName = "Cat Tembok Dulux 5L";
        String productDescription = "Cat tembok interior warna putih doff";
        int productPrice = 120000;
        int productStock = 50;

        product.setProductId(productId);
        product.setProductName(productName);
        product.setProductDescription(productDescription);
        product.setProductPrice(productPrice);
        product.setProductStock(productStock);

        assertEquals("d255e6f5-448d-42b9-9f77-287082e5ab80", product.getProductId());
        assertEquals("Cat Tembok Dulux 5L", product.getProductName());
        assertEquals("Cat tembok interior warna putih doff", product.getProductDescription());
        assertEquals(120000, product.getProductPrice());
        assertEquals(50, product.getProductStock());
    }


    // ================ UNHAPPY PATH TESTS ================ //

    @Test
    void testSetNullProductId() {
        assertThrows(IllegalArgumentException.class, () -> {
            product.setProductId(null);
        }, "Product id cannot be null");
    }

    @Test
    void testSetNullProductName() {
        assertThrows(IllegalArgumentException.class, () -> {
            product.setProductName(null);
        }, "Product name cannot be null");
    }

    @Test
    void testSetEmptyProductName() {
        assertThrows(IllegalArgumentException.class, () -> {
            product.setProductName("");
        }, "Product name cannot be empty");
    }

    @Test
    void testSetNullProductDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            product.setProductDescription(null);
        }, "Product description cannot be null");
    }

    @Test
    void testSetNegativeProductPrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            product.setProductPrice(-400000);
        }, "Product price cannot be negative");
    }

    @Test
    void testSetNegativeProductStock() {
        assertThrows(IllegalArgumentException.class, () -> {
            product.setProductStock(-5);
        }, "Product stock cannot be negative");
    }

}
