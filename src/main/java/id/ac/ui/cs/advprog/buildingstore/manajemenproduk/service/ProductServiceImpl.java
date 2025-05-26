package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repository;

    public void setRepository(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product create(Product product) {
        return repository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Product findById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Product update(String id, Product updatedProduct) {
        if (updatedProduct == null) {
            throw new IllegalArgumentException("Updated product cannot be null");
        }

        Product existingProduct = repository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setProductDescription(updatedProduct.getProductDescription());
            existingProduct.setProductPrice(updatedProduct.getProductPrice());
            existingProduct.setProductStock(updatedProduct.getProductStock());
            return repository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public void reduceStock(Product product, int quantity) {
        if (product.getProductStock() < quantity) {
            throw new IllegalArgumentException("Stok tidak cukup");
        }
        product.setProductStock(product.getProductStock() - quantity);
    }

    @Override
    public void increaseStock(Product product, int quantity) {
        product.setProductStock(product.getProductStock() + quantity);
    }


}
