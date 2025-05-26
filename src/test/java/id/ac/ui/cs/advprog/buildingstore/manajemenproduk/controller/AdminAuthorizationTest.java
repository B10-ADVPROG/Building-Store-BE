package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthorizationService;
import id.ac.ui.cs.advprog.buildingstore.manajemenproduk.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(ProductController.class)
public class AdminAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private AuthorizationService authorizationService;

    @Test
    public void testCreateNoToken() throws Exception {
        mockMvc.perform(post("/product/create/")
                        .contentType("application/json")
                        .content("{\"productName\":\"Test Product\",\"productPrice\":1000}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
    }

    @Test
    public void testCreateInvalidToken() throws Exception {

        mockMvc.perform(post("/product/create/")
                        .header("Authorization", "Bearer invalidtoken")
                        .contentType("application/json")
                        .content("{\"productName\":\"Test Product\",\"productPrice\":1000}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
    }

    @Test
    public void testReadAllNoToken() throws Exception {
        mockMvc.perform(get("/product/"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
    }

    @Test
    public void testReadAllInvalidToken() throws Exception {
        mockMvc.perform(get("/product/")
                        .header("Authorization", "Bearer invalidtoken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
    }


    @Test
    public void testReadNoToken() throws Exception {
        mockMvc.perform(get("/product/detail/5dcb62d8-801a-4a4d-8bc8-15e1bc2f1fed/"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
    }

    @Test
    public void testReadInvalidToken() throws Exception {

        mockMvc.perform(get("/product/detail/5dcb62d8-801a-4a4d-8bc8-15e1bc2f1fed/")
                        .header("Authorization", "Bearer invalidtoken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
    }

    @Test
    public void testUpdateNoToken() throws Exception {
        mockMvc.perform(put("/product/edit/5dcb62d8-801a-4a4d-8bc8-15e1bc2f1fed/")
                        .contentType("application/json")
                        .content("{\"productName\":\"Updated Product\",\"productPrice\":2000}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
    }

    @Test
    public void testUpdateInvalidToken() throws Exception {

        mockMvc.perform(put("/product/edit/5dcb62d8-801a-4a4d-8bc8-15e1bc2f1fed/")
                        .header("Authorization", "Bearer invalidtoken")
                        .contentType("application/json")
                        .content("{\"productName\":\"Updated Product\",\"productPrice\":2000}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
    }

    @Test
    public void testDeleteNoToken() throws Exception {
        mockMvc.perform(delete("/product/delete/5dcb62d8-801a-4a4d-8bc8-15e1bc2f1fed/"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
    }

    @Test
    public void testDeleteInvalidToken() throws Exception {

        mockMvc.perform(delete("/product/delete/5dcb62d8-801a-4a4d-8bc8-15e1bc2f1fed/")
                        .header("Authorization", "Bearer invalidtoken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
    }
}
