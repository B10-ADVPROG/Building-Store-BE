package id.ac.ui.cs.advprog.buildingstore.manajemenpelanggan.service;

import id.ac.ui.cs.advprog.buildingstore.manajemenpelanggan.model.Customer;
import id.ac.ui.cs.advprog.buildingstore.manajemenpelanggan.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Integer id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }

    @Override
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Integer id, Customer customerDetails) {
        Customer existingCustomer = getCustomerById(id);
        if (existingCustomer == null) {
            return null;
        }
        
        // Update fields
        existingCustomer.setName(customerDetails.getName());
        existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());
        existingCustomer.setAddress(customerDetails.getAddress());
        existingCustomer.setEmail(customerDetails.getEmail());
        
        return customerRepository.update(existingCustomer);
    }

    @Override
    public void deleteCustomer(Integer id) {
        customerRepository.delete(id);
    }

    @Override
    public List<Customer> searchCustomers(String name) {
        return customerRepository.findByNameContaining(name);
    }
}