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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@TestPropertySource(properties = {"auth.enabled=false"})
@ActiveProfiles("test")
@WebMvcTest(ProductController.class)
public class DeleteProductTest {

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
    void testDeleteSuccess() throws Exception {
        when(productService.findById(productId)).thenReturn(existingProduct);
        Mockito.doNothing().when(productService).delete(productId);

        mockMvc.perform(delete("/product/delete/" + productId + "/")
                        .header("Authorization", validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product deleted successfully"));
    }

    @Test
    void testDeleteFailed() throws Exception {
        Mockito.doThrow(new RuntimeException("Product not found")).when(productService).delete("notfound");

        mockMvc.perform(delete("/product/delete/notfound/")
                        .header("Authorization", validToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Failed to delete product: Product not found"));
    }

}
