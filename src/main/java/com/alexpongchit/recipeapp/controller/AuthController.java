package com.alexpongchit.recipeapp.controller;

import com.alexpongchit.recipeapp.dto.LoginRequest;
import com.alexpongchit.recipeapp.dto.RegisterRequest;
import com.alexpongchit.recipeapp.model.User;
import com.alexpongchit.recipeapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User savedUser = userService.registerUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully with ID: " + savedUser.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
        return userService.findByUsername(request.getUsername())
                .map(user -> {
                    if (user.getPassword().equals(request.getPassword())) {
                        return ResponseEntity.ok("Login successful");
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found"));
    }
}