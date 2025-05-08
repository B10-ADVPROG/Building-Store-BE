package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.factory.AdminFactory;
import id.ac.ui.cs.advprog.buildingstore.authentication.factory.KasirFactory;
import id.ac.ui.cs.advprog.buildingstore.authentication.factory.UserFactory;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestAuthController.class)
public class RegisterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testRegisterSuccessfulKasir() throws Exception {
        RegisterRequest request = new RegisterRequest("kasir@example.com", "Budi Kasir", "pass123", "kasir");
        UserFactory factory = new KasirFactory();
        User newUser = factory.createUser("kasir@example.com", "Budi Kasir", "pass123");

        // Simulate service behavior
        when(authService.registerUser(any(User.class))).thenReturn(newUser);

        // Perform the POST request
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("New Kasir is registered successfully"));
    }

    @Test
    public void testRegisterSuccessfulAdministrator() throws Exception {
        RegisterRequest request = new RegisterRequest("admin@example.com", "Sari Admin", "adminpass", "administrator");
        UserFactory factory = new AdminFactory();
        User newUser = factory.createUser(request.getEmail(), request.getFullname(), request.getPassword());

        when(authService.registerUser(any(User.class))).thenReturn(newUser);

        // Perform the POST request
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("New Administrator is registered successfully"));
    }

    @Test
    public void testRegisterInvalidRole() throws Exception {
        RegisterRequest request = new RegisterRequest("test@example.com", "Invalid Role", "password", "manager");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Unknown role: manager"));
    }

    @Test
    public void testRegisterEmptyEmail() throws Exception {
        RegisterRequest request = new RegisterRequest("", "No Email", "password", "kasir");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Email cannot be empty"));
    }
}
