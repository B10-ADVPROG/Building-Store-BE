package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository repository;

    public void setRepository(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product create(Product product) {
        return repository.create(product);
    }

    @Override
    public List<Product> findAll() {
        Iterator<Product> iterator = repository.findAll();
        List<Product> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        return result;
    }

    @Override
    public Product findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Product update(String id, Product updatedProduct) {
        return repository.update(id, updatedProduct);
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }


}
