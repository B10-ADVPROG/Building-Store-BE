package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductService;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.dto.CreateTransactionRequest;
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

    private Product product1;
    private Product product2;

    @BeforeEach
    public void setUp() {
        validToken = "Bearer Token";

        product1 = new Product.Builder()
                .productName("Semen")
                .productPrice(10000)
                .productStock(20)
                .build();
        product2 = new Product.Builder()
                .productName("Cat")
                .productPrice(15000)
                .productStock(30)
                .build();

        Mockito.when(productService.create(eq(product1))).thenReturn(product1);
        Mockito.when(productService.create(eq(product2))).thenReturn(product2);
        productService.create(product1);
        productService.create(product2);

    }

    @Test
    public void testCreateTransactionSuccess() throws Exception {
        Map<String, Integer> productsMap = new HashMap<>();
        productsMap.put(product1.getProductId(), 2);
        productsMap.put(product2.getProductId(), 3);

        CreateTransactionRequest request = new CreateTransactionRequest(productsMap);

        Map<Product, Integer> productObjMap = new HashMap<>();
        productObjMap.put(product1, 2);
        productObjMap.put(product2, 3);

        SalesTransaction mockTransaction = new SalesTransaction(productObjMap);
        mockTransaction.setStatus(SalesTransaction.Status.IN_PROGRESS);

        Mockito.when(transactionService.createRequest(any(CreateTransactionRequest.class)))
                .thenReturn(mockTransaction);

        mockMvc.perform(post("/transaction/create/")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transaction is created successfully"));
    }


    @Test
    public void testCreateTransactionEmptyProductMap() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest(new HashMap<>());

        mockMvc.perform(post("/transaction/create/")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.products").value("Product map must not be empty"));
    }

}
