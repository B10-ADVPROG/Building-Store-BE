package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.dto.LoginRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.factory.UserFactory;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.JwtService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        // Halaman register dari API Frontend (sementara menggunakan template spring boot)
        return "authentication/register";
    }


    @GetMapping("/login")
    public String loginPage(Model model) {
        // Halaman login dari API Frontend (sementara menggunakan template spring boot)
        return "authentication/login";
    }
}

@RestController
@RequestMapping("/auth")
class RestAuthController {
    @Autowired
    private AuthService authService;

//    @Autowired
//    private JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<Object> registerPost(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = UserFactory.createUser(
                    registerRequest.getRole(),
                    registerRequest.getEmail(),
                    registerRequest.getFullname(),
                    registerRequest.getPassword()
            );

            authService.registerUser(user);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("message", "New " + user.getRole() + " is registered successfully"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("errors", List.of(e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("errors", List.of("Unexpected error: " + e.getMessage())));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        boolean isAuthenticated = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

        if (isAuthenticated) {
            User user = authService.findByEmail(loginRequest.getEmail());

            // // Generate JWT token
            // String token = jwtService.generateToken(user);

            // For simplicity, token will just be generated manually
            String token = "Token";

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);  // Add token to response

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> error = new HashMap<>();
            error.put("errors", List.of("Email or password is incorrect"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(400).body("Authorization header is missing or malformed");
            }

            String token = authorizationHeader.substring(7);

//            // Verifikasi token
//            if (jwtService.isTokenValid(token)) {
//                // Token valid, invalidate session
//                session.invalidate();
//                return ResponseEntity.ok().body("Successfully logged out");
//            } else {
//                return ResponseEntity.status(401).body("Invalid or expired token");
//            }

            // For simplicity (only for debug)
            if (token.equals("Token")) {
                return ResponseEntity.ok().body(Map.of("message", "Logout successful"));
            } else {
                return ResponseEntity.status(401).body("Unauthorized");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during logout");
        }
    }



}


