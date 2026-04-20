package com.alexpongchit.recipeapp.controller;

import com.alexpongchit.recipeapp.exception.ResourceNotFoundException;
import com.alexpongchit.recipeapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns the database ID of the currently authenticated user.
     *
     * This endpoint is used by the frontend when creating recipes so it can
     * associate new recipes with the logged-in user.
     */
    @GetMapping("/me")
    public ResponseEntity<Long> getCurrentUserId(Authentication authentication) {
        String username = authentication.getName();

        Long userId = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username))
                .getId();

        return ResponseEntity.ok(userId);
    }
}