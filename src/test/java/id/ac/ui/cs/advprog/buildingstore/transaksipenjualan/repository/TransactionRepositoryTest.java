package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.repository;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model.SalesTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionRepositoryTest {

    private TransactionRepository transactionRepository;
    private SalesTransaction transaction1;
    private SalesTransaction transaction2;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        transactionRepository = new TransactionRepository();

        product1 = new Product.Builder()
                .productName("Cat Tembok")
                .productDescription("Cat untuk dinding")
                .productPrice(100000)
                .productStock(50)
                .build();

        product2 = new Product.Builder()
                .productName("Kuas Cat")
                .productDescription("Kuas ukuran sedang")
                .productPrice(20000)
                .productStock(100)
                .build();

        transaction1 = new SalesTransaction();
        transaction1.setProducts(Map.of(product1, 2));
        transaction1.setStatus(SalesTransaction.Status.IN_PROGRESS);
        transaction1.generateIdAndTimestamp();

        transaction2 = new SalesTransaction();
        transaction2.setProducts(Map.of(product2, 3));
        transaction2.setStatus(SalesTransaction.Status.COMPLETED);
        transaction2.generateIdAndTimestamp();

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
    }

    @Test
    void testSaveAndFindById() {
        SalesTransaction found = transactionRepository.findById(transaction1.getTransactionId());
        assertNotNull(found);
        assertEquals(transaction1.getTransactionId(), found.getTransactionId());
        assertEquals(2, found.getProducts().get(product1));
    }

    @Test
    void testFindAll() {
        List<SalesTransaction> allTransactions = transactionRepository.findAll();
        assertEquals(2, allTransactions.size());
        assertTrue(allTransactions.contains(transaction1));
        assertTrue(allTransactions.contains(transaction2));
    }

    @Test
    void testDelete() {
        transactionRepository.delete(transaction1.getTransactionId());
        assertNull(transactionRepository.findById(transaction1.getTransactionId()));
        assertEquals(1, transactionRepository.findAll().size());
    }

    @Test
    void testUpdate() {
        transaction1.setStatus(SalesTransaction.Status.COMPLETED);
        transactionRepository.update(transaction1.getTransactionId(), transaction1);

        SalesTransaction updated = transactionRepository.findById(transaction1.getTransactionId());
        assertNotNull(updated);
        assertEquals(SalesTransaction.Status.COMPLETED, updated.getStatus());
    }
}
