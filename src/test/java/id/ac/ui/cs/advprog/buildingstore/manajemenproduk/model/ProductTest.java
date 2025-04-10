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
        product = new Product();
        product.setProductId("960c03bc-9e06-41c7-aa50-cb582cee3e27");
        product.setProductName("Semen Tiga Roda 50 kg");
        product.setProductDescription("Semen berkualitas tinggi untuk konstruksi bangunan");
        product.setProductPrice(80000);
        product.setProductStock(100);
    }

    @Test
    void testGetProduct() {
        assertEquals("960c03bc-9e06-41c7-aa50-cb582cee3e27", product.getProductId());
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





}
