package com.alexpongchit.recipeapp.service;

import com.alexpongchit.recipeapp.model.User;
import com.alexpongchit.recipeapp.repository.UserRepository;
import com.alexpongchit.recipeapp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void registerUser_shouldSaveAndReturnUser() {
        User user = new User();
        user.setUsername("alex");
        user.setEmail("alex@example.com");
        user.setPassword("password123");

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.registerUser(user);

        assertNotNull(savedUser);
        assertEquals("alex", savedUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findByUsername_shouldReturnUserWhenFound() {
        User user = new User();
        user.setUsername("alex");

        when(userRepository.findByUsername("alex")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("alex");

        assertTrue(result.isPresent());
        assertEquals("alex", result.get().getUsername());
    }

    @Test
    void existsByEmail_shouldReturnTrueWhenEmailExists() {
        User user = new User();
        user.setEmail("alex@example.com");

        when(userRepository.findByEmail("alex@example.com")).thenReturn(Optional.of(user));

        boolean exists = userService.existsByEmail("alex@example.com");

        assertTrue(exists);
    }
}