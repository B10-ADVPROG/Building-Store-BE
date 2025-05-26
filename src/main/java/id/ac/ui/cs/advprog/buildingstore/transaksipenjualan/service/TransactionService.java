package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.service;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductService;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.dto.CreateTransactionRequest;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductService productService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, ProductService productService) {
        this.transactionRepository = transactionRepository;
        this.productService = productService;
    }

    public SalesTransaction createRequest(CreateTransactionRequest request) {
        Map<Product, Integer> productMap = new HashMap<>();

        for (Map.Entry<String, Integer> entry : request.getProducts().entrySet()) {
            String productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productService.findById(productId);

            productMap.put(product, quantity);
        }

        SalesTransaction transaction = new SalesTransaction();
        transaction.setProducts(productMap);

        return create(transaction);
    }

    public SalesTransaction create(SalesTransaction transaction) {
        transaction.getProducts().forEach((product, quantity) -> {
            productService.reduceStock(product, (int) quantity);
        });

        return transactionRepository.save(transaction);
    }


    public List<SalesTransaction> findAll() {
        return transactionRepository.findAll();
    }

    public SalesTransaction findById(String id) {
        return transactionRepository.findById(id);
    }

    public void deleteById(String id) {
        SalesTransaction transaction = transactionRepository.findById(id);
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction with id " + id + " not found");
        }

        transaction.getProducts().forEach((product, quantity) -> {
            productService.increaseStock(product, (int) quantity);
        });

        transactionRepository.deleteById(id);
    }

    public void update(String id) {
        SalesTransaction transaction = transactionRepository.findById(id);
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction with id " + id + " not found");
        }
        if (transaction.getStatus() != SalesTransaction.Status.IN_PROGRESS) {
            throw new IllegalArgumentException("Only transactions with status IN_PROGRESS can be updated");
        }
        transaction.setStatus(SalesTransaction.Status.COMPLETED);
        transactionRepository.update(id, transaction);
    }
}
