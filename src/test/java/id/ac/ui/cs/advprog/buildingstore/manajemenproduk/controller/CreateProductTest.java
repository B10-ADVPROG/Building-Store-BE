package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.dto.ProductRequestDto;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class CreateProductTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private String validToken;

    @BeforeEach
    public void setUp() {
        validToken = "Bearer Token";
    }

    @Test
    public void testCreateProductSuccess() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Semen Tiga Roda 50 kg", "Semen berkualitas tinggi untuk konstruksi bangunan", 80000, 100);
        Product product = new Product.Builder()
                                .productName(request.getProductName())
                                .productDescription(request.getProductDescription())
                                .productPrice(request.getProductPrice())
                                .productStock(request.getProductStock())
                                .build();

        when(productService.create(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/create")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("New product is created successfully"));
    }

    @Test
    public void testCreateProductUnauthorized() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Semen Tiga Roda", "Deskripsi", 80000, 100);

        mockMvc.perform(post("/product/create")
                        .header("Authorization", "Bearer invalid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
    }

    @Test
    public void testCreateProductEmptyDescription() {
        CreateProductRequest request = new CreateProductRequest("Semen", "", 80000, 100);
        Product product = new Product.Builder()
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .productPrice(request.getProductPrice())
                .productStock(request.getProductStock())
                .build();

        when(productService.create(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/create")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("New product is created successfully"));
    }

    @Test
    public void testCreateProductEmptyStock() {
        CreateProductRequest request = new CreateProductRequest("Semen", "Deskripsi", 80000, null);
        Product product = new Product.Builder()
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .productPrice(request.getProductPrice())
                .productStock(request.getProductStock()) // Stock is null
                .build();

        when(productService.create(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/create")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("New product is created successfully"));
    }

    @Test
    public void testCreateProductEmptyName() {
        CreateProductRequest request = new CreateProductRequest("", "Deskripsi", 80000, 100);

        mockMvc.perform(post("/product/create")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.productName").value("Product name is required"));
    }

    @Test
    public void testCreateProductEmptyPrice() {
        CreateProductRequest request = new CreateProductRequest("Semen", "Deskripsi", null, 100);

        mockMvc.perform(post("/product/create")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.productPrice").value("Product price is required"));
    }

}
