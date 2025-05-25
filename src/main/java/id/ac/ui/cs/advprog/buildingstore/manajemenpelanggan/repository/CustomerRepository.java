package id.ac.ui.cs.advprog.buildingstore.manajemenpelanggan.repository;

import id.ac.ui.cs.advprog.buildingstore.manajemenpelanggan.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CustomerRepository {

    private final List<Customer> customers = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    public Optional<Customer> findById(Integer id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public Customer save(Customer customer) {
        customer.setId(idCounter.getAndIncrement());
        customers.add(customer);
        return customer;
    }

    public Customer update(Customer customer) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId().equals(customer.getId())) {
                customers.set(i, customer);
                return customer;
            }
        }
        return null;
    }

    public void delete(Integer id) {
        customers.removeIf(c -> c.getId().equals(id));
    }

    public List<Customer> findByNameContaining(String name) {
        String searchTerm = name.toLowerCase();
        List<Customer> result = new ArrayList<>();
        
        for (Customer customer : customers) {
            if (customer.getName().toLowerCase().contains(searchTerm)) {
                result.add(customer);
            }
        }
        return result;
    }
}