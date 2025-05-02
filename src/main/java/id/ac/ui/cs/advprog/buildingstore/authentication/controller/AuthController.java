package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        // Halaman register dari API Frontend
        model.addAttribute("user", new User());
        return "authentication/register";
    }


    @GetMapping("/login")
    public String loginPage(Model model) {
        // Halaman login dari API Frontend (sementara menggunakan template langsung)
        model.addAttribute("user", new User());
        return "authentication/login";
    }
}

@RestController
@RequestMapping("/auth")
class RestAuthController {
    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<Object> registerPost(@RequestBody User user) {
        try {
            authService.registerUser(user);
            String roleName = user.getRole();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("message", "New " + roleName + " is registered successfully"));
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

}


