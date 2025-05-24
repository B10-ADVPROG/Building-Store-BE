package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl service;

    private Product product1, product2;

    @BeforeEach
    void setUp() {
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
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        Product createdProduct = service.create(product1);

        assertNotNull(createdProduct);
        assertEquals(product1.getProductName(), createdProduct.getProductName());
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void testFindAllProduct() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> allProducts = service.findAll();

        assertFalse(allProducts.isEmpty());
        assertEquals(2, allProducts.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindProductById() {
        when(productRepository.findById(product2.getProductId())).thenReturn(Optional.of(product2));

        Product foundProduct = service.findById(product2.getProductId());

        assertNotNull(foundProduct);
        assertEquals(product2.getProductName(), foundProduct.getProductName());
        verify(productRepository, times(1)).findById(product2.getProductId());
    }

    @Test
    void testEditProduct() {
        when(productRepository.findById(product1.getProductId())).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product2);

        Product updatedProduct = service.update(product1.getProductId(), product2);

        assertNotNull(updatedProduct);
        assertEquals(product2.getProductName(), updatedProduct.getProductName());
        verify(productRepository, times(1)).findById(product1.getProductId());
        verify(productRepository, times(1)).save(product2);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(product1.getProductId());

        service.delete(product1.getProductId());

        verify(productRepository, times(1)).deleteById(product1.getProductId());
    }

    @Test
    void testFindNonExistingProduct() {
        when(productRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        Product foundProduct = service.findById("non-existent-id");

        assertNull(foundProduct);
    }

    @Test
    void testUpdateNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.update(product1.getProductId(), null);
        });
    }
}