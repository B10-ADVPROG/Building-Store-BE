package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthorizationService;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.repository.ProductRepository;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(properties = {"auth.enabled=false"})
@ActiveProfiles("test")
@WebMvcTest(ProductController.class)
public class ProductDetailTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private AuthorizationService authorizationService;

    private Product existingProduct;
    private String validToken;
    private String productId;

    @BeforeEach
    void setUp() {
        validToken = "Bearer dummy-token";
        existingProduct = new Product.Builder()
                .productName("Semen Tiga Roda")
                .productDescription("Semen berkualitas")
                .productPrice(80000)
                .productStock(100)
                .build();
        productId = existingProduct.getProductId();
    }

    @Test
    void testGetProductDetailSuccess() throws Exception {
        when(productService.findById(productId)).thenReturn(existingProduct);

        mockMvc.perform(get("/product/detail/" + productId + "/")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.productName").value("Semen Tiga Roda"))
                .andExpect(jsonPath("$.productDescription").value("Semen berkualitas"))
                .andExpect(jsonPath("$.productPrice").value(80000))
                .andExpect(jsonPath("$.productStock").value(100));
    }

    @Test
    void testGetProductDetailNotFound() throws Exception {
        when(productService.findById("notfound")).thenReturn(null);

        mockMvc.perform(get("/product/detail/notfound/")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
