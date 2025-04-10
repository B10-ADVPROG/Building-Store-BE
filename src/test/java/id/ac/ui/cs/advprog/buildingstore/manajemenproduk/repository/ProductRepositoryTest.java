package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.repository;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRepositoryTest {
    private ProductRepository productRepository;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();

        product1 = new Product();
        product1.setProductId("120052d9-aa02-4026-a70f-d0cb45ef4a81");
        product1.setProductName("Laptop Gaming ASUS ROG");
        product1.setProductDescription("Laptop Gaming ASUS ROG");
        product1.setProductDescription("Laptop dengan GPU GeForce RTX 4090, RAM 32 GB, SSD 2TB");
        product1.setProductPrice(30000000);
        product1.setProductStock(10);

        product2 = new Product();
        product2.setProductId("d255e6f5-448d-42b9-9f77-287082e5ab80");
        product2.setProductName("Laptop Macbook Pro");
        product2.setProductDescription("Laptop dengan prosesor M3 chip, memori 8GB, dan penyimpanan 1TB");
        product2.setProductPrice(25000000);
        product2.setProductStock(50);
    }

    @Test
    void testCreateProduct() {
        productRepository.create(product1);
        productRepository.create(product2);

        List<Product> products = productRepository.getProducts();

        assertEquals(2, products.size());
    }

    @Test
    void testReadProduct() {
        productRepository.create(product1);

        Iterator<Product> productIterator = productRepository.findAll();
        Product readProduct = productIterator.next();
        assertEquals("120052d9-aa02-4026-a70f-d0cb45ef4a81", readProduct.getProductId());
        assertEquals("Laptop Gaming ASUS ROG", readProduct.getProductName());
        assertEquals("Laptop dengan GPU GeForce RTX 4090, RAM 32 GB, SSD 2TB", readProduct.getProductDescription());
        assertEquals(30000000, readProduct.getProductPrice());
        assertEquals(10, readProduct.getProductStock());
    }

    @Test
    void testUpdateProduct() {
        productRepository.create(product1);

        Product updatedProduct = new Product();
        updatedProduct.setProductId(product1.getProductId());
        updatedProduct.setProductName("Macbook Pro");
        updatedProduct.setProductDescription("Laptop dengan prosesor M3 chip, memori 8GB, dan penyimpanan 1TB");
        updatedProduct.setProductPrice(25000000);
        updatedProduct.setProductStock(50);


        productRepository.update(product1.getProductId(), updatedProduct);
    }

    @Test
    void testDeleteProduct() {
        productRepository.create(product1);

        productRepository.delete(product1.getProductId());
        List<Product> products = productRepository.getProducts();
        Iterator<Product> productIterator = products.iterator();

        assertTrue(products.isEmpty());
        assertFalse(productIterator.hasNext());
    }


}
