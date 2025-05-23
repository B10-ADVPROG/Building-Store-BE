package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.service;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductService;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.LinkedHashMap;
import java.util.List;
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


    @Test
    void testFindById() {
        Map<Product, Integer> products = Map.of(product1, 1);
        SalesTransaction transaction = new SalesTransaction(products);
        String id = transaction.getTransactionId();

        when(transactionRepository.findById(id)).thenReturn(transaction);

        SalesTransaction found = transactionService.findById(id);

        verify(transactionRepository).findById(id);
        assertNotNull(found);
        assertEquals(id, found.getTransactionId());
    }

    @Test
    void testFindAll() {
        Map<Product, Integer> products1 = Map.of(product1, 1);
        Map<Product, Integer> products2 = Map.of(product2, 2);

        SalesTransaction transaction1 = new SalesTransaction(products1);
        SalesTransaction transaction2 = new SalesTransaction(products2);

        List<SalesTransaction> transactions = List.of(transaction1, transaction2);

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<SalesTransaction> foundList = transactionService.findAll();

        verify(transactionRepository).findAll();
        assertEquals(2, foundList.size());
        assertTrue(foundList.contains(transaction1));
        assertTrue(foundList.contains(transaction2));
    }

    @Test
    void testUpdateStatusValid() {
        Map<Product, Integer> products = Map.of(product1, 1);
        SalesTransaction transaction = new SalesTransaction(products);
        String id = transaction.getTransactionId();

        transactionService.update(id);

        SalesTransaction updated = transactionService.findById(id);

        assertEquals(SalesTransaction.Status.COMPLETED, updated.getStatus());
    }

    @Test
    void testUpdateStatusInvalid() {
        Map<Product, Integer> products = Map.of(product1, 1);
        SalesTransaction transaction = new SalesTransaction(products);
        transaction.setStatus(SalesTransaction.Status.COMPLETED);
        String id = transaction.getTransactionId();

        assertThrows(IllegalArgumentException.class, () -> transactionService.update(id));

        SalesTransaction updated = transactionService.findById(id);
        assertEquals(SalesTransaction.Status.COMPLETED, updated.getStatus());
    }








}
