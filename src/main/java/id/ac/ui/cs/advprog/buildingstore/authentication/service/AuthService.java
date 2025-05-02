package id.ac.ui.cs.advprog.buildingstore.authentication.service;


import id.ac.ui.cs.advprog.buildingstore.authentication.model.Administrator;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.Kasir;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;


    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public User registerUser(User newUser) {
        User existingUser = authRepository.findByEmail(newUser.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("Email is already taken");
        }

        return authRepository.save(newUser);
    }

    public boolean authenticateUser(String email, String password) {
        User user = authRepository.findByEmail(email);
        if (user != null) {
            return password.equals(user.getPassword());
        }
        return false;
    }

    public void logoutUser() {
    }



}