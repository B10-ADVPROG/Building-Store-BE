package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.repository;

import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Repository
public class TransactionRepository {
    private List<Transaction> transactionData = new ArrayList<>();

    public Transaction create(Transaction transaction) {
        transaction.setTransactionId(java.util.UUID.randomUUID().toString());
        transaction.setDate(new Date());
        transactionData.add(transaction);
        return transaction;
    }

    public Iterator<Transaction> findAll() {
        return transactionData.iterator();
    }

    // Like findById
    public Transaction getTransactionDetails(String transactionId) {
        for (Transaction transaction : transactionData) {
            if (transaction.getTransactionId().equals(transactionId)) {
                return transaction;
            }
        }
        return null;
    }
}
