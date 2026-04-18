package com.alexpongchit.recipeapp.controller;

import com.alexpongchit.recipeapp.dto.LoginRequest;
import com.alexpongchit.recipeapp.dto.LoginResponse;
import com.alexpongchit.recipeapp.dto.RegisterRequest;
import com.alexpongchit.recipeapp.model.User;
import com.alexpongchit.recipeapp.security.JwtService;
import com.alexpongchit.recipeapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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
                    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        String token = jwtService.generateToken(user.getUsername());
                        return ResponseEntity.ok(new LoginResponse(token));
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found"));
    }
}