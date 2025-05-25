//package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.dto.EditProductDTO;
//import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model.Product;
//import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@TestPropertySource(properties = {"auth.enabled=false"})
//@ActiveProfiles("test")
//@WebMvcTest(ProductController.class)
//public class EditProductTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private ProductService productService;
//
//    private String validToken;
//    private Product existingProduct;
//    private String productId;
//
//    @BeforeEach
//    public void setUp() {
//        validToken = "Bearer Token";
//        existingProduct = new Product.Builder()
//                .productName("Semen Tiga Roda")
//                .productDescription("Semen berkualitas")
//                .productPrice(80000)
//                .productStock(100)
//                .build();
//
//        productId = existingProduct.getProductId();
//    }
//
//    @Test
//    public void testEditProductSuccess() throws Exception {
//        EditProductDTO requestBody = new EditProductDTO();
//        requestBody.setNewProductName("Semen Gresik");
//        requestBody.setNewProductDescription("Semen kuat");
//        requestBody.setNewProductPrice(90000);
//        requestBody.setNewProductStock(120);
//
//        when(productService.findById(productId)).thenReturn(existingProduct);
//        when(productService.update(Mockito.eq(productId), Mockito.any(Product.class))).thenReturn(existingProduct);
//
//        mockMvc.perform(put("/product/edit/" + productId + "/")
//                        .header("Authorization", validToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestBody)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Product updated successfully"));
//    }
//
//    @Test
//    public void testEditProductUnauthorized() throws Exception {
//        EditProductDTO requestBody = new EditProductDTO();
//        requestBody.setNewProductName("Semen Gresik");
//        requestBody.setNewProductDescription("Semen kuat");
//        requestBody.setNewProductPrice(90000);
//        requestBody.setNewProductStock(120);
//
//        mockMvc.perform(put("/product/edit/" + productId + "/")
//                        .header("Authorization", "Bearer invalid")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestBody)))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
//    }
//
//    @Test
//    public void testEditProductNotFound() throws Exception {
//        EditProductDTO requestBody = new EditProductDTO();
//        requestBody.setNewProductName("Semen Gresik");
//        requestBody.setNewProductDescription("Semen kuat");
//        requestBody.setNewProductPrice(90000);
//        requestBody.setNewProductStock(120);
//
//        when(productService.findById("notfound")).thenReturn(null);
//
//        mockMvc.perform(put("/product/edit/notfound/")
//                        .header("Authorization", validToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestBody)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Product not found"));
//    }
//
//    @Test
//    public void testEditProductMissingField() throws Exception {
//        EditProductDTO requestBody = new EditProductDTO();
//        requestBody.setNewProductName("Semen Gresik");
//        requestBody.setNewProductDescription("Semen kuat");
//        requestBody.setNewProductPrice(90000);
//        // newProductStock not set (simulasi missing field)
//
//        when(productService.findById(productId)).thenReturn(existingProduct);
//
//        mockMvc.perform(put("/product/edit/" + productId + "/")
//                        .header("Authorization", validToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestBody)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").exists());
//    }
//}
