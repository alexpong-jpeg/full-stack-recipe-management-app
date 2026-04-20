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
 * REST controller for authentication operations such as registration and login.
 *
 * This controller supports the application's JWT-based authentication flow by
 * validating credentials and returning a token that the frontend can use for
 * protected API requests.
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
     * Registers a new user after confirming that the username and email
     * are not already in use.
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

        // The raw password is passed into the service layer, where it is encoded
        // before being persisted to the database.
        user.setPassword(request.getPassword());

        User savedUser = userService.registerUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully with ID: " + savedUser.getId());
    }

    /**
     * Authenticates a user by username and password and returns a JWT
     * when the credentials are valid.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
        return userService.findByUsername(request.getUsername())
                .map(user -> {
                    // Compare the raw password from the request against the
                    // encoded password stored for the user.
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