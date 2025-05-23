package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.repository;

import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model.SalesTransaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionRepository {

    private final Map<String, SalesTransaction> storage = new HashMap<>();

    public SalesTransaction save(SalesTransaction transaction) {
        storage.put(transaction.getTransactionId(), transaction);
        return transaction;
    }

    public SalesTransaction findById(String id) {
        return storage.get(id);
    }

    public List<SalesTransaction> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void deleteById(String id) {
        storage.remove(id);
    }

    public SalesTransaction update(String id, SalesTransaction updatedTransaction) {
        if (storage.containsKey(id)) {
            storage.put(id, updatedTransaction);
            return updatedTransaction;
        }
        return null;
    }
}
