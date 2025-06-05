package com.bkr.shopen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bkr.shopen.model.User;
import com.bkr.shopen.services.UserService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public String index() {
        return "User API is working!";
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        
        return ResponseEntity.ok(user);

    }

    @PostMapping("")
    public ResponseEntity<Map<String, String>> addNewUser(@Valid @RequestBody User user) {
        
       userService.saveUser(user); 

       return new ResponseEntity<>(Map.of("message", "User created successfully"), HttpStatus.CREATED);
    }
    
}
