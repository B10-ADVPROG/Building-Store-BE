package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;

import java.util.List;

public interface ProductService {
    public Product create(Product product);
    public List<Product> findAll();
    public Product findById(String id);
    public Product update(String id, Product product);
    public void delete(String id);
    public void reduceStock(Product product, int quantity);
    public void increaseStock(Product product, int quantity);
}
