package com.alexpongchit.recipeapp.service.impl;

import com.alexpongchit.recipeapp.model.User;
import com.alexpongchit.recipeapp.repository.UserRepository;
import com.alexpongchit.recipeapp.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Default implementation of the UserService interface.
 *
 * This service supports user registration, password encoding, and lookup
 * operations used by authentication and recipe ownership workflows.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Encodes a new user's password before saving the user to the database.
     */
    @Override
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Finds a user by username.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Finds a user by email address.
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Returns true when the username is already in use.
     */
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Returns true when the email address is already in use.
     */
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Finds a user by database identifier.
     */
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}