package id.ac.ui.cs.advprog.buildingstore.authentication.service;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    JwtService jwtService;
    User user;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();
        user = new User();
        user.setEmail("test@example.com");
        user.setRole("administrator");
    }

    @Test
    void generateAndValidate() {
        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
        assertEquals("Administrator", jwtService.extractRole(token));
    }

    @Test
    void invalidate() {
        String token = jwtService.generateToken(user);
        assertTrue(jwtService.isTokenValid(token));
        jwtService.invalidateToken(token);
        assertFalse(jwtService.isTokenValid(token));
    }
}
