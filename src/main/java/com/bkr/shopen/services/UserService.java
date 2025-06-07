package com.bkr.shopen.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bkr.shopen.error.ResourceNotFoundExceptionErr;
import com.bkr.shopen.model.User;
import com.bkr.shopen.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     *
     * @param userRepository the repository for user data access
     */
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if(id <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }

        if (!(id instanceof Long)) {
            throw new IllegalArgumentException("User ID must be of type Long");
        }

        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExceptionErr("User not found with id: " + id));
    }

 
    public User findOrCreateUser(User user) {
        return userRepository.findByEmail(user.getEmail())
            .orElseGet(() -> userRepository.save(user));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        
        if(!user.isPresent()) throw new UsernameNotFoundException("User not found with username: " + username);

        final User userDetails = user.get();

        return org.springframework.security.core.userdetails.User.builder()
            .username(userDetails.getUsername())
            .password(userDetails.getPassword())    
            .roles(getRoles(user))
            .build();
        
    }

    private String[] getRoles(Optional<User> user) {
        if (!user.isPresent() || user.get().getRole() == null || user.get().getRole().isBlank()) {
            return new String[] {"USER"}; 
        }

        return user.get().getRole().split(","); 
    }
}