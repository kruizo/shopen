package com.bkr.shopen.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bkr.shopen.error.PermissionDeniedExceptionErr;

@RestController
public class HomeController {

    @Value("${spring.application.name}")
    private String  AppName;

    @GetMapping("/home")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Welcome to " + AppName);
    }

    @GetMapping("/admin/")
    public ResponseEntity<String> adminPage() {
        
        return ResponseEntity.ok("On admin access page admin");
    }
    @GetMapping("/user")
    public ResponseEntity<String>  userPage() {
        org.springframework.security.core.Authentication authentication =
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
            authentication.getPrincipal().equals("anonymousUser")) {
            throw new PermissionDeniedExceptionErr("Access denied: User is not authenticated");
        }

        return ResponseEntity.ok("On user access page user");
    }
}
