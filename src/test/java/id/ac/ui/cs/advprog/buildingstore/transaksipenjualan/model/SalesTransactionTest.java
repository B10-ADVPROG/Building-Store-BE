package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SalesTransactionTest {

    private Product product1;
    private Product product2;
    private SalesTransaction transaction;

    @BeforeEach
    void setUp() {
        product1 = new Product.Builder()
                .productName("Cat Dulux")
                .productDescription("Cat tembok kualitas premium")
                .productPrice(150000)
                .productStock(50)
                .build();

        product2 = new Product.Builder()
                .productName("Paku 5cm")
                .productDescription("Paku baja")
                .productPrice(1000)
                .productStock(1000)
                .build();

        Map<Product, Integer> productMap = new HashMap<>();
        productMap.put(product1, 2);
        productMap.put(product2, 10);

        transaction = new SalesTransaction();
        transaction.setProducts(productMap);
        transaction.setStatus(SalesTransaction.Status.IN_PROGRESS);
        transaction.generateIdAndTimestamp();
    }

    @Test
    void testTransactionInitialization() {
        assertNotNull(transaction.getTransactionId());
        assertNotNull(transaction.getTimestamp());
        assertEquals(SalesTransaction.Status.IN_PROGRESS, transaction.getStatus());
        assertEquals(2, transaction.getProducts().size());
    }

    @Test
    void testTotalAmountCalculation() {
        int expected = (2 * 150000) + (10 * 1000);
        assertEquals(expected, transaction.getTotalAmount());
    }

    @Test
    void testChangeTransactionStatus() {
        transaction.setStatus(SalesTransaction.Status.COMPLETED);
        assertEquals(SalesTransaction.Status.COMPLETED, transaction.getStatus());
    }

    @Test
    void testDefaultProgress() {
        SalesTransaction t = new SalesTransaction(transaction.getProducts());
        assertEquals(SalesTransaction.Status.IN_PROGRESS, t.getStatus());
    }

    @Test
    void testDefaultProgressWithNoArgConstructor() {
        SalesTransaction t = new SalesTransaction();
        assertEquals(SalesTransaction.Status.IN_PROGRESS, t.getStatus());
    }
}
