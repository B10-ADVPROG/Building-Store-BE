package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.repository;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepository {
    private List<Product> products = new ArrayList<>();

    public Product create(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Cannot create null product");
        }
        products.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return products.iterator();
    }

    public Product findById(String id) {
        for (Product product : products) {
            if (product.getProductId().equals(id)) return product;
        }
        return null;
    }

    public Product update(String id, Product updatedProduct) {
        if (updatedProduct == null) {
            throw new IllegalArgumentException("Cannot update with null product");
        }

        Product product = findById(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found for update");
        }

        product.setProductName(updatedProduct.getProductName());
        product.setProductDescription(updatedProduct.getProductDescription());
        product.setProductPrice(updatedProduct.getProductPrice());
        product.setProductStock(updatedProduct.getProductStock());

        return product;
    }

    public void delete(String id) {
        products.removeIf(product -> product.getProductId().equals(id));
    }

}
