package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.service;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductService;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private ProductService productService;
    private TransactionService transactionService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        productService = mock(ProductService.class);
        transactionService = new TransactionService(transactionRepository, productService);

        product1 = new Product.Builder()
                .productName("Besi Hollow")
                .productDescription("Untuk rangka")
                .productPrice(200000)
                .productStock(50)
                .build();

        product2 = new Product.Builder()
                .productName("Lem Fox")
                .productDescription("Lem kuat")
                .productPrice(15000)
                .productStock(100)
                .build();
    }

    @Test
    void testCreateTransactionAndReduceStock() {
        Map<Product, Integer> products = new LinkedHashMap<>();
        products.put(product1, 2);
        products.put(product2, 3);

        SalesTransaction transaction = new SalesTransaction(products);

        when(transactionRepository.save(any(SalesTransaction.class))).thenReturn(transaction);

        SalesTransaction saved = transactionService.create(transaction);

        verify(productService).reduceStock(product1, 2);
        verify(productService).reduceStock(product2, 3);
        verify(transactionRepository).save(transaction);

        assertEquals(2, saved.getProducts().size());
        assertNotNull(saved.getTimestamp());
        assertEquals(SalesTransaction.Status.IN_PROGRESS, saved.getStatus());
    }

    @Test
    void testCreateTransactionFailsIfStockInsufficient() {
        doThrow(new IllegalArgumentException("Stok tidak cukup"))
                .when(productService).reduceStock(product1, 1000);

        Map<Product, Integer> products = new LinkedHashMap<>();
        products.put(product1, 1000);

        SalesTransaction transaction = new SalesTransaction(products);

        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.create(transaction);
        });

        verify(productService).reduceStock(product1, 1000);
        verify(transactionRepository, never()).save(any());
    }
}
