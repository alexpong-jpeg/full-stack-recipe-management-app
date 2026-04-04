package com.alexpongchit.recipeapp.service;

import com.alexpongchit.recipeapp.model.User;

import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findById(Long id);
}