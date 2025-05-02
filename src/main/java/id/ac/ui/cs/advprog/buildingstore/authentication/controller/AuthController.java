package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;


@Controller
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    @GetMapping("/register")
    public String registerPage(Model model) {  // Menampilkan halaman create product
        return "authentication/register";
    }




}
