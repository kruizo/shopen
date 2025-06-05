package com.bkr.shopen.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bkr.shopen.error.ResourceNotFoundExceptionErr;
import com.bkr.shopen.model.User;
import com.bkr.shopen.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExceptionErr("User not found with id: " + id));
    }

    public void saveUser(User user) {
        // Encrypt the password before saving
        if(user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
    }

    public User findOrCreateUser(User user) {
        return userRepository.findByEmail(user.getEmail())
            .orElseGet(() -> userRepository.save(user));
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}