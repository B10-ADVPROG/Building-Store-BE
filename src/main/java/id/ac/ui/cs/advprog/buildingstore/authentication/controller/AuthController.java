package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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


