package id.ac.ui.cs.advprog.buildingstore.manajemenpelanggan.service;

import id.ac.ui.cs.advprog.buildingstore.manajemenpelanggan.model.Customer;
import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer getCustomerById(Integer id);
    Customer addCustomer(Customer customer);
    Customer updateCustomer(Integer id, Customer customer);
    void deleteCustomer(Integer id);
    List<Customer> searchCustomers(String name);
}