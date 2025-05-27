package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductService;
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

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(TransactionController.class)
public class ReadTransactionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private ProductService productService;

    private String validToken;
    private SalesTransaction transaction1;
    private SalesTransaction transaction2;

    @BeforeEach
    void setUp() {
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

        transaction1 = new SalesTransaction(Map.of(product1, 2));
        transaction2 = new SalesTransaction(Map.of(product2, 3));

        when(transactionService.findAll()).thenReturn(List.of(transaction1, transaction2));
    }

    @Test
    public void testReadTransaction() throws Exception {
        mockMvc.perform(get("/transaction/")
                        .header("Authorization", validToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].transactionId").value(transaction1.getTransactionId()))
                .andExpect(jsonPath("$[1].transactionId").value(transaction2.getTransactionId()));
    }

    @Test
    public void testReadTransactionEmpty() throws Exception {
        when(transactionService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/transaction/")
                        .header("Authorization", validToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

}
