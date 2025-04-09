package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.service;

import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model.Transaction;
import java.util.List;

public interface TransactionService {
    public Transaction create(Transaction transaction);
    public List<Transaction> findAll();
    public Transaction getTransactionDetails(String transactionId);
}
