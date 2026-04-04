package com.alexpongchit.recipeapp.controller;

import com.alexpongchit.recipeapp.dto.LoginRequest;
import com.alexpongchit.recipeapp.dto.RegisterRequest;
import com.alexpongchit.recipeapp.model.User;
import com.alexpongchit.recipeapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication-related operations.
 *
 * Phase I includes basic user registration and login endpoints so the project
 * can demonstrate working backend functionality before JWT-based security is
 * fully implemented in later phases.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user after verifying that the username and email are unique.
     */
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

        // Phase I stores the password directly for simplicity during early testing.
        // This should be replaced with password encoding in a later phase.
        user.setPassword(request.getPassword());

        User savedUser = userService.registerUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully with ID: " + savedUser.getId());
    }

    /**
     * Performs a basic login check using username and password.
     *
     * This is a temporary Phase I implementation intended for backend validation.
     * Later phases should replace this approach with encoded passwords and JWT tokens.
     */
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