package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.controller;

import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ViewProductsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product.Builder()
                .productName("Semen Portland Tipe 1")
                .productDescription("Semen berkualitas tinggi untuk konstruksi bangunan")
                .productPrice(75000)
                .productStock(100)
                .build();

        product2 = new Product.Builder()
                .productName("Cat Dinding Premium")
                .productDescription("Cat dinding dengan daya tahan tinggi")
                .productPrice(150000)
                .productStock(50)
                .build();
    }

    @Test
    void testViewProducts() throws Exception {
        Mockito.when(productService.findAll()).thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/product")
                        .header("Authorization", "Bearer Token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Semen Portland Tipe 1")))
                .andExpect(jsonPath("$[0].price", is(75000)))
                .andExpect(jsonPath("$[1].name", is("Cat Dinding Premium")))
                .andExpect(jsonPath("$[1].stock", is(50)));
    }

    @Test
    void testInvalidToken() throws Exception {
        mockMvc.perform(get("/product")
                        .header("Authorization", "Bearer InvalidToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Invalid or missing token")));
    }

    @Test
    void testNoToken() throws Exception {
        mockMvc.perform(get("/product"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Invalid or missing token")));
    }
}
