package id.ac.ui.cs.advprog.transaksipenjualan.service;

import id.ac.ui.cs.advprog.transaksipenjualan.model.Transaction;
import id.ac.ui.cs.advprog.transaksipenjualan.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction create(Transaction transaction) {
        transactionRepository.create(transaction);
        return transaction;
    }

    @Override
    public List<Transaction> findAll() {
        Iterator<Transaction> transactionIterator = transactionRepository.findAll();
        List<Transaction> allTransaction = new ArrayList<>();
        transactionIterator.forEachRemaining(allTransaction::add);
        return allTransaction;
    }

    @Override
    public Transaction getTransactionDetails(String transactionId) {
        return transactionRepository.getTransactionDetails(transactionId);
    }
}
