package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.repository.ProductRepository;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductServiceImpl service;

    Product product1, product2;

    @BeforeEach
    void setUp() {
        service = new ProductServiceImpl();
        ProductRepository productRepository = new ProductRepository();
        service.setRepository(productRepository);

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


    // ================ HAPPY PATH TESTS ================ //

    @Test
    void testCreateProduct() {
        service.create(product1);
        service.create(product2);
        List<Product> allProducts = service.findAll();

        assertFalse(allProducts.isEmpty());
        assertEquals(2, allProducts.size());
    }

    @Test
    void testFindAllProduct() {
        service.create(product1);
        service.create(product2);

        List<Product> allProducts = service.findAll();

        assertEquals(product1, allProducts.get(0));
        assertEquals(product2, allProducts.get(1));
    }

    @Test
    void testFindProductById() {
        service.create(product1);
        service.create(product2);

        Product targetProduct = service.findById(product2.getProductId());

        assertEquals("d255e6f5-448d-42b9-9f77-287082e5ab80", targetProduct.getProductId());
        assertEquals("Cat Tembok Dulux 5L", targetProduct.getProductName());
        assertEquals("Cat tembok interior warna putih doff", targetProduct.getProductDescription());
        assertEquals(120000, targetProduct.getProductPrice());
        assertEquals(50, targetProduct.getProductStock());
    }

    @Test
    void testEditProduct() {
        service.create(product1);

        Product updatedProduct = new Product();
        updatedProduct.setProductId(product1.getProductId());
        updatedProduct.setProductName("Cat Tembok Dulux 5L");
        updatedProduct.setProductDescription("Cat tembok interior warna putih doff");
        updatedProduct.setProductPrice(120000);
        updatedProduct.setProductStock(50);

        service.update(product1.getProductId(), updatedProduct);

        Product targetProduct = service.findById(product1.getProductId());
        assertEquals("Cat Tembok Dulux 5L", targetProduct.getProductName());
        assertEquals("Cat tembok interior warna putih doff", targetProduct.getProductDescription());
        assertEquals(120000, targetProduct.getProductPrice());
        assertEquals(50, targetProduct.getProductStock());
    }

    @Test
    void testDeleteProduct() {
        service.create(product1);
        service.delete(product1.getProductId());

        List<Product> allProduct = service.findAll();
        assertTrue(allProduct.isEmpty());
    }

    // ================ UNHAPPY PATH TESTS ================ //

    @Test
    void testCreateNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.create(null);
        }, "Cannot create null product");
    }

    @Test
    void testFindNonExistingProduct() {
        Product targetProduct = service.findById("non-existent-id");
        assertNull(targetProduct);
    }

    @Test
    void testUpdateNullProduct() {
        service.create(product1);
        assertThrows(IllegalArgumentException.class, () -> {
            service.update(product1.getProductId(), null);
        }, "Cannot update with null product");
    }

}
