package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.authentication.dto.LoginRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.factory.KasirFactory;
import id.ac.ui.cs.advprog.buildingstore.authentication.factory.UserFactory;
import id.ac.ui.cs.advprog.buildingstore.authentication.factory.UserFactoryTest;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.Kasir;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestAuthController.class)
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testLoginSuccessful() throws Exception {
        // Register the user first
        RegisterRequest registerRequest = new RegisterRequest("kasir@example.com", "Budi Kasir", "kasirpass", "kasir");
        UserFactory factory = new KasirFactory();
        User user = factory.createUser(registerRequest.getEmail(), registerRequest.getFullname(), registerRequest.getPassword());
        authService.registerUser(user);

        String token = "Token";

        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        LoginRequest loginRequest = new LoginRequest(email, password);

        when(authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(true);
        when(authService.findByEmail(loginRequest.getEmail())).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(token);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    public void testLoginInvalidCredentials() throws Exception {
        // Create a new user
        RegisterRequest registerRequest = new RegisterRequest("kasir@example.com", "Budi Kasir", "kasirpass", "kasir");
        UserFactory factory = new KasirFactory();
        User user = factory.createUser(registerRequest.getEmail(), registerRequest.getFullname(), registerRequest.getPassword());
        authService.registerUser(user);

        LoginRequest loginRequest = new LoginRequest("wrong@example.com", "wrongpass");

        when(authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors[0]").value("Email or password is incorrect"));
    }

    @Test
    public void testLoginEmptyEmail() throws Exception {
        LoginRequest request = new LoginRequest("", "somepass");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Email cannot be empty"));
    }

    @Test
    public void testLoginEmptyPassword() throws Exception {
        LoginRequest request = new LoginRequest("kasir@example.com", "");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Password cannot be empty"));
    }
}
