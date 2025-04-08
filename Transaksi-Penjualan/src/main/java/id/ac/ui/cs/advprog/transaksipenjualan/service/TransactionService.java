package id.ac.ui.cs.advprog.transaksipenjualan.service;

import id.ac.ui.cs.advprog.transaksipenjualan.model.Transaction;
import java.util.List;

public interface TransactionService {
    public Transaction create(Transaction transaction);
    public List<Transaction> findAll();
    public Transaction getTransactionDetails(String transactionId);

}
