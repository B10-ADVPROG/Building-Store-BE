package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("960c03bc-9e06-41c7-aa50-cb582cee3e27");
        product.setProductName("Laptop Gaming ASUS ROG");
        product.setProductDescription("Laptop dengan GPU GeForce RTX 4090, RAM 32 GB, SSD 2TB");
        product.setProductPrice(30000000);
        product.setProductStock(10);
    }

    @Test
    void testGetProduct() {
        assertEquals("960c03bc-9e06-41c7-aa50-cb582cee3e27", product.getProductId());
        assertEquals("Laptop Gaming ASUS ROG", product.getProductName());
        assertEquals("Laptop dengan GPU GeForce RTX 4090, RAM 32 GB, SSD 2TB", product.getProductDescription());
        assertEquals(30000000, product.getProductPrice());
        assertEquals(10, product.getProductStock());

    }

    @Test
    void testSetProduct() {
        String productId = "d255e6f5-448d-42b9-9f77-287082e5ab80";
        String productName = "Laptop Macbook Pro";
        String productDescription = "Laptop dengan prosesor M3 chip, memori 8GB, dan penyimpanan 1TB";
        int productPrice = 25000000;
        int productStock = 50;

        product.setProductId(productId);
        product.setProductName(productName);
        product.setProductDescription(productDescription);
        product.setProductPrice(productPrice);
        product.setProductStock(productStock);

        assertEquals("d255e6f5-448d-42b9-9f77-287082e5ab80", product.getProductId());
        assertEquals("Laptop Macbook Pro", product.getProductName());
        assertEquals("Laptop dengan prosesor M3 chip, memori 8GB, dan penyimpanan 1TB", product.getProductDescription());
        assertEquals(25000000, product.getProductPrice());
        assertEquals(50, product.getProductStock());
    }





}
