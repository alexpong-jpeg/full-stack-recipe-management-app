package com.alexpongchit.recipeapp.service;

import com.alexpongchit.recipeapp.model.User;
import com.alexpongchit.recipeapp.repository.UserRepository;
import com.alexpongchit.recipeapp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void registerUser_shouldEncodePasswordSaveAndReturnUser() {
        User user = new User();
        user.setUsername("alex");
        user.setEmail("alex@example.com");
        user.setPassword("password123");

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");

        User savedUser = new User();
        savedUser.setUsername("alex");
        savedUser.setEmail("alex@example.com");
        savedUser.setPassword("encodedPassword123");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(user);

        assertNotNull(result);
        assertEquals("alex", result.getUsername());
        assertEquals("encodedPassword123", result.getPassword());

        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
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

    @Test
    void existsByUsername_shouldReturnTrueWhenUsernameExists() {
        User user = new User();
        user.setUsername("alex");

        when(userRepository.findByUsername("alex")).thenReturn(Optional.of(user));

        boolean exists = userService.existsByUsername("alex");

        assertTrue(exists);
    }

    @Test
    void findById_shouldReturnUserWhenFound() {
        User user = new User();
        user.setId(1L);
        user.setUsername("alex");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("alex", result.get().getUsername());
    }
}