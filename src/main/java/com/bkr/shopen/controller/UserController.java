package com.bkr.shopen.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.bkr.shopen.dto.UserDto;
import com.bkr.shopen.model.UserRole;
import com.bkr.shopen.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RequestMapping("/users")
@RestController
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    /**
     * Constructor for UserController.
     *
     * @param userService the service for user operations
     * @return void
     */

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/test")
    public String index() {
        return "User API is working!";
    }

    @GetMapping("/me")
    public ResponseEntity<?> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        UserDto user = userService.getUserById(userId);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<UserRole> getUserRoles(@PathVariable Long userId) {
        UserRole role = userService.getUserRole(userId);

        return ResponseEntity.ok(role);
    }

    @PutMapping("/{userId}/roles")
    public ResponseEntity<String> updateUserRoles(@PathVariable Long userId, @RequestBody UserRole role) {
        userService.updateUserRoles(userId, role);
        return ResponseEntity.ok("User roles updated successfully");
    }
    
}
