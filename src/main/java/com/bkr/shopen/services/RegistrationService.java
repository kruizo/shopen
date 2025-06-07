package com.bkr.shopen.services;

import org.springframework.stereotype.Service;
import com.bkr.shopen.model.User;
import com.bkr.shopen.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor for UserService.
     *
     * @param userRepository the repository for user data access
     * @param passwordEncoder the password encoder for hashing passwords
     */
    public RegistrationService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if(user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with this username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
    }

}