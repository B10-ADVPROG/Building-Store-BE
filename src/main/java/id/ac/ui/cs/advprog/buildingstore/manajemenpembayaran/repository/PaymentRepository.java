package id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.repository;

import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByCustomerId(String customerId);
    List<Payment> findByTransactionId(String transactionId);
    List<Payment> findByStatus(String status);
}