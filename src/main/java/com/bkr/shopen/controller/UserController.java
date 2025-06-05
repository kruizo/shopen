package com.bkr.shopen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bkr.shopen.model.User;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @RequestMapping("/")
    public String index() {
        return "User API is working!";
    }

    @GetMapping
    public List<User> getAllUsers() {
        return List.of(
                new User("John Doe", "john.doe@example.com", "password123", "123 Main St", "555-1234"),
                new User("Jane Smith", "jane.smith@example.com", "securePass", "456 Oak Ave", "555-5678"),
                new User("Alice Johnson", "alice.johnson@example.com", "alicePwd", "789 Pine Rd", "555-9012")
        );
    }
}
