package id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.service;

import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository repository;

    @Override
    public Payment create(Payment payment) {
        return repository.save(payment);
    }

    @Override
    public List<Payment> findAll() {
        return repository.findAll();
    }

    @Override
    public Payment findById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Payment updateStatus(String id, String status) {
        Payment existingPayment = repository.findById(id).orElse(null);
        if (existingPayment != null) {
            existingPayment.setStatus(status);
            return repository.save(existingPayment);
        }
        return null;
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<Payment> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public List<Payment> findByTransactionId(String transactionId) {
        return repository.findByTransactionId(transactionId);
    }
}