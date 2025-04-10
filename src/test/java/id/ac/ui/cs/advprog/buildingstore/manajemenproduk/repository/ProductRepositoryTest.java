package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.repository;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        product1.setProductName("Semen Tiga Roda 50 kg");
        product1.setProductDescription("Semen berkualitas tinggi untuk konstruksi bangunan");
        product1.setProductPrice(80000);
        product1.setProductStock(200);

        product2 = new Product();
        product2.setProductId("d255e6f5-448d-42b9-9f77-287082e5ab80");
        product2.setProductName("Cat Tembok Dulux 5L");
        product2.setProductDescription("Cat tembok interior warna putih doff");
        product2.setProductPrice(120000);
        product2.setProductStock(50);
    }

    @Test
    void testCreateProduct() {
        productRepository.create(product1);
        productRepository.create(product2);

        Iterator<Product> products = productRepository.findAll();
        assertTrue(products.hasNext());

        int size = 0;
        while (products.hasNext()) {
            products.next();
            size++;
        }
        assertEquals(2, size);
    }

    @Test
    void testReadProduct() {
        productRepository.create(product1);

        Iterator<Product> productIterator = productRepository.findAll();
        Product readProduct = productIterator.next();
        assertEquals("120052d9-aa02-4026-a70f-d0cb45ef4a81", readProduct.getProductId());
        assertEquals("Semen Tiga Roda 50 kg", readProduct.getProductName());
        assertEquals("Semen berkualitas tinggi untuk konstruksi bangunan", readProduct.getProductDescription());
        assertEquals(80000, readProduct.getProductPrice());
        assertEquals(10, readProduct.getProductStock());
    }

    @Test
    void testFindProductById() {
        productRepository.create(product1);

        Product targetProduct = productRepository.findById(product1.getProductId());
        assertEquals(targetProduct.getProductName(), product1.getProductName());
        assertEquals(targetProduct.getProductDescription(), product1.getProductDescription());
        assertEquals(targetProduct.getProductPrice(), product1.getProductPrice());
        assertEquals(targetProduct.getProductStock(), product1.getProductStock());
    }

    @Test
    void testUpdateProduct() {
        productRepository.create(product1);

        Product updatedProduct = new Product();
        updatedProduct.setProductId(product1.getProductId());
        updatedProduct.setProductName("Cat Tembok Dulux 5L");
        updatedProduct.setProductDescription("Cat tembok interior warna putih doff");
        updatedProduct.setProductPrice(120000);
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
