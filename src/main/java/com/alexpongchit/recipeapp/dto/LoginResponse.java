package com.alexpongchit.recipeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO returned after successful authentication containing the JWT token
 * the client should use for protected requests.
 */
@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
}