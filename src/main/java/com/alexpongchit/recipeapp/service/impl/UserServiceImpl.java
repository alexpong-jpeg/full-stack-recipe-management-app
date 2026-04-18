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
 * Phase I uses this service to support user registration and user lookup
 * operations required by authentication and recipe ownership logic.
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
     * Saves a new user to the database.
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
     * Finds a user by email.
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Checks whether a username is already in use.
     */
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Checks whether an email is already in use.
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