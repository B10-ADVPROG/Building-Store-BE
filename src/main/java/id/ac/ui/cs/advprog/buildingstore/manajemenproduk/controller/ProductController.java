package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.controller;


import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.dto.CreateProductRequest;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/product")
@Validated
public class ProductController {
    @Autowired
    private ProductService productService;

    private boolean isTokenValid(String header) {
        if (header == null || !header.startsWith("Bearer ")) return false;
        String token = header.substring(7);
//        return jwtService.isTokenValid(token);
        // For simplicity
        return token.equals("Token");
    }

    @GetMapping("")
    public ResponseEntity<Object> allProducts(@RequestHeader("Authorization") String authHeader) {
        if (!isTokenValid(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or missing token"));
        }

        List<Product> products = productService.findAll();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Product product : products) {
            Map<String, Object> productMap = new HashMap<>();
            productMap.put("id", product.getProductId());
            productMap.put("name", product.getProductName());
            productMap.put("description", product.getProductDescription());
            productMap.put("price", product.getProductPrice());
            productMap.put("stock", product.getProductStock());
            response.add(productMap);
        }

        return ResponseEntity.ok(response);
    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<Map<String, Object>> getProductDetail(@PathVariable String id, @RequestHeader("Authorization") String authHeader) {
        if (!isTokenValid(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or missing token"));
        }

        Product product = productService.findById(id);

        if (! (product == null)) {
            Map<String, Object> response = new HashMap<>();
            response.put("productId", product.getProductId());
            response.put("productName", product.getProductName());
            response.put("productDescription", product.getProductDescription());
            response.put("productPrice", product.getProductPrice());
            response.put("productStock", product.getProductStock());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createProduct(@Valid @RequestBody CreateProductRequest requestBody, @RequestHeader("Authorization") String authHeader) {
        if (!isTokenValid(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or missing token"));
        }

        try {
            System.out.println("Checkpoint 1");
            Product newProduct = new Product.Builder()
                    .productName(requestBody.getProductName())
                    .productDescription(requestBody.getProductDescription())
                    .productPrice(requestBody.getProductPrice())
                    .productStock(requestBody.getProductStock())
                    .build();
            System.out.println("Checkpoint 2");

            productService.create(newProduct);

            return ResponseEntity.ok(Map.of("message", "New product is created successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", "Invalid request: " + e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateProduct(@PathVariable("id") String id, @RequestBody Map<String, Object> requestBody, @RequestHeader("Authorization") String authHeader) {
        if (!isTokenValid(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or missing token"));
        }

        try {
            Product existingProduct = productService.findById(id);
            if (existingProduct == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Product not found"));
            }

            String name = (String) requestBody.get("newProductName");
            String description = (String) requestBody.get("newProductDescription");
            int price = Integer.parseInt(requestBody.get("newProductPrice").toString());
            int stock = Integer.parseInt(requestBody.get("newProductStock").toString());

            existingProduct.setProductName(name);
            existingProduct.setProductDescription(description);
            existingProduct.setProductPrice(price);
            existingProduct.setProductStock(stock);

            productService.update(id, existingProduct);

            return ResponseEntity.ok(Map.of("message", "Product updated successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", "Failed to update product: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id, @RequestHeader("Authorization") String authHeader) {
        if (!isTokenValid(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }
        try {
            productService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", "Failed to delete product: " + e.getMessage()));
        }
    }


}
