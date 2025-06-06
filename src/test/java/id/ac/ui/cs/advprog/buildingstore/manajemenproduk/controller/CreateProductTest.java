package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthorizationService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.dto.CreateProductRequest;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(properties = {"auth.enabled=false"})
@ActiveProfiles("test")
@WebMvcTest(ProductController.class)
public class CreateProductTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private AuthorizationService authorizationService;

    private String validToken;

    @BeforeEach
    public void setup() {
        validToken = "Bearer dummy-token";
    }

    @Test
    public void testCreateProductSuccess() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Semen Tiga Roda 50 kg", "Semen berkualitas tinggi", 80000, 100);
        Product product = new Product.Builder()
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .productPrice(request.getProductPrice())
                .productStock(request.getProductStock())
                .build();

        when(productService.create(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/create/")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("New product is created successfully"));
    }

    @Test
    public void testCreateProductEmptyDescription() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Semen", "", 80000, 100);
        Product product = new Product.Builder()
                .productName(request.getProductName())
                .productDescription("")
                .productPrice(request.getProductPrice())
                .productStock(request.getProductStock())
                .build();

        when(productService.create(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/create/")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("New product is created successfully"));
    }

    @Test
    public void testCreateProductEmptyStock() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Semen", "Deskripsi", 80000, null);

        mockMvc.perform(post("/product/create/")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("New product is created successfully"));
    }

    @Test
    public void testCreateProductEmptyName() throws Exception {
        CreateProductRequest request = new CreateProductRequest("", "Deskripsi", 80000, 100);

        mockMvc.perform(post("/product/create/")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.productName").value("Product name is required"));
    }

    @Test
    public void testCreateProductEmptyPrice() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Semen", "Deskripsi", null, 100);

        mockMvc.perform(post("/product/create/")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.productPrice").value("Product price is required"));
    }
}
