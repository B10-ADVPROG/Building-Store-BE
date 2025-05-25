package id.ac.ui.cs.advprog.buildingstore.authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    public static final String ROLE_ADMIN = "administrator";
    public static final String ROLE_KASIR = "kasir";

    @Autowired
    private JwtService jwtService;

    public boolean authorizeAdmin(String token) {
        if (!jwtService.isTokenValid(token)) {
            System.out.println("Token invalid");
            return false;
        }
        try {
            String role = jwtService.extractRole(token);
            return ROLE_ADMIN.equals(role.toLowerCase());
        } catch (Exception e) {
            System.out.println("Failed to extract role");
            return false;
        }
    }

    public boolean authorizeKasir(String token) {
        if (!jwtService.isTokenValid(token)) {
            return false;
        }
        try {
            String role = jwtService.extractRole(token);
            return ROLE_KASIR.equals(role.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }
}