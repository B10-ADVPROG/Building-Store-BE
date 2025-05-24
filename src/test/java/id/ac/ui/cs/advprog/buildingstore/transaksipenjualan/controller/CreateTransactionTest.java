package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductService;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.dto.CreateTransactionRequest;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.dto.CreateTransactionResponse;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class CreateTransactionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private ProductService productService;

    private String validToken;

    private String productId1;
    private String productId2;

    @BeforeEach
    public void setUp() {
        validToken = "Bearer Token";

        Product product1 = new Product.Builder()
                .productName("Semen")
                .productPrice(10000)
                .productStock(20)
                .build();
        Product product2 = new Product.Builder()
                .productName("Cat")
                .productPrice(15000)
                .productStock(30)
                .build();

        String productId1 = product1.getProductId();
        String productId2 = product2.getProductId();

        Mockito.when(productService.create(eq(product1))).thenReturn(product1);
        Mockito.when(productService.create(eq(product2))).thenReturn(product2);
        productService.create(product1);
        productService.create(product2);

    }

    @Test
    public void testCreateTransactionSuccess() throws Exception {
        Map<String, Integer> productMap = new HashMap<>();
        productMap.put(productId1, 2);
        productMap.put(productId2, 3);

        CreateTransactionRequest request = new CreateTransactionRequest(productMap);

        Mockito.when(transactionService.create(any(CreateTransactionRequest.class)))
                .thenReturn(Map.of("message", "Transaction is created successfully"));

        mockMvc.perform(post("/transaction/create")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transaction is created successfully"));
    }


    @Test
    public void testCreateTransactionUnauthorized() throws Exception {
        Map<String, Integer> productMap = new HashMap<>();
        productMap.put("product1", 1);

        CreateTransactionRequest request = new CreateTransactionRequest(productMap);

        mockMvc.perform(post("/transaction/create")
                        .header("Authorization", "Bearer invalid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
    }

    @Test
    public void testCreateTransactionEmptyRequest() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest(new HashMap<>());

        mockMvc.perform(post("/transaction/create")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.products").value("Product map must not be empty"));
    }
}
