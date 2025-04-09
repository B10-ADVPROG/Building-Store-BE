package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.service;

import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model.Product;
import java.util.List;

public interface ProductService {
    Product create(Product product);
    List<Product> findAll();
    Product findById(String productId);
}
