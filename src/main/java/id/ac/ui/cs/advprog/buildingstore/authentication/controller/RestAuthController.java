package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.dto.LoginRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.factory.AdminFactory;
import id.ac.ui.cs.advprog.buildingstore.authentication.factory.KasirFactory;
import id.ac.ui.cs.advprog.buildingstore.authentication.factory.UserFactory;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class RestAuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @Autowired
    public RestAuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerPost(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        // Handle validation errors first
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            UserFactory userFactory = getUserFactory(registerRequest.getRole());
            User user = userFactory.createUser(
                    registerRequest.getEmail(),
                    registerRequest.getFullname(),
                    registerRequest.getPassword()
            );

            User registeredUser = authService.registerUser(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "New " + registeredUser.getRole() + " is registered successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("errors", List.of(e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("errors", List.of("Registration failed: " + e.getMessage())));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginRequest loginRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        boolean isAuthenticated = authService.authenticateUser(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        if (!isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("errors", List.of("Email or password is incorrect")));
        }

        User user = authService.findByEmail(loginRequest.getEmail());
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok()
                .body(Map.of(
                        "message", "Login successful",
                        "token", token
                ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Authorization header is missing or malformed"));
            }

            String token = authorizationHeader.substring(7);

            if (!jwtService.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired token"));
            }

            jwtService.invalidateToken(token);

            return ResponseEntity.ok()
                    .body(Map.of("message", "Successfully logged out"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "An error occurred during logout: " + e.getMessage()));
        }
    }

    private UserFactory getUserFactory(String role) {
        switch (role.toLowerCase()) {
            case "kasir":
                return new KasirFactory();
            case "administrator":
                return new AdminFactory();
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}