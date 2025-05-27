package id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.service;

import id.ac.ui.cs.advprog.buildingstore.manajemenpembayaran.model.Payment;

import java.util.List;

public interface PaymentService {
    Payment create(Payment payment);
    List<Payment> findAll();
    Payment findById(String id);
    Payment updateStatus(String id, String status);
    void delete(String id);
    List<Payment> findByCustomerName(String customerName);
}