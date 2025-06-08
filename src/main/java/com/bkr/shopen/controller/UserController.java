package com.bkr.shopen.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bkr.shopen.dto.UserDto;
import com.bkr.shopen.mapper.UserMapper;
import com.bkr.shopen.model.User;
import com.bkr.shopen.services.UserService;

import java.util.List;

@RequestMapping("/users")
@RestController
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

    @GetMapping("/test")
    public String index() {
        return "User API is working!";
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(UserMapper.toDto(currentUser));
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        System.out.println("Fetching all users");
        List<UserDto> users = userService.getAllUsers()
            .stream()
            .map(UserMapper::toDto)
            .toList();

        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }
    
}
