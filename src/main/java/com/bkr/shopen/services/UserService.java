package com.bkr.shopen.services;

import java.util.List;
import java.util.Optional;

import com.bkr.shopen.mapper.UserMapper;
import org.springframework.stereotype.Service;

import com.bkr.shopen.dto.UserDto;
import com.bkr.shopen.error.ConflictExceptionErr;
import com.bkr.shopen.error.InternalServerExceptionErr;
import com.bkr.shopen.error.ResourceNotFoundExceptionErr;
import com.bkr.shopen.model.User;
import com.bkr.shopen.model.UserRole;
import com.bkr.shopen.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
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

    public List<UserDto> getAllUsers() {
        List<UserDto> usersDto = userRepository.findAll()
            .stream()
            .map(UserMapper::toDto)
            .toList();

        return usersDto;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        Optional<User> user = userRepository.findByUsername(username);

        final User userDetails = user.orElseThrow(() -> new UsernameNotFoundException("User not found."));

        return org.springframework.security.core.userdetails.User.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .roles(getRoles(userDetails))
                .build();
    }

    public UserDto getUserById(Long userId) {
        return UserMapper.toDto(getUserOrThrow(userId));
    }


    public UserRole getUserRole(Long userId) {
        return getUserOrThrow(userId).getRole();
    }

    @Transactional
    public void updateUserRoles(Long userId, UserRole role) {
        User user = getUserOrThrow(userId);
        user.setRole(role);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictExceptionErr("Role update conflict: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new InternalServerExceptionErr("Unexpected error updating user role", e);
        } 
    }

    private String[] getRoles(User user) {
    // Remove the "ROLE_" prefix here
        return new String[] { user.getRole().name() };
    }

    private User getUserOrThrow(Long userId) {
        if (userId == null) throw new IllegalArgumentException("User ID cannot be null");
        if (userId <= 0) throw new IllegalArgumentException("User ID must be a positive number");

        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundExceptionErr("User not found with ID: " + userId));
    }
}