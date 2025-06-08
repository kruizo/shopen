package com.bkr.shopen.controller;
import com.bkr.shopen.dto.UserDto;
import com.bkr.shopen.services.UserService;
import com.bkr.shopen.services.auth.AuthService;
import com.bkr.shopen.services.auth.JwtService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bkr.shopen.dto.LoginUserDto;
import com.bkr.shopen.dto.RegisterUserDto;
import com.bkr.shopen.dto.VerifyUserDto;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/auth/")
public class AuthenticationController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService  userService;
    
    /**
     * Constructor for RegistrationController.
     *
     * @param authService the service for authentication operations
     * @param jwtService the service for JWT token generation
     */



    public AuthenticationController(AuthService authService, JwtService jwtService, UserService userService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<String> registerPage() {
        return ResponseEntity.ok("On register page with method called with param: ");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticate(@Valid @RequestBody LoginUserDto loginUserDto){
        UserDto authenticatedUser = authService.authenticate(loginUserDto);

        UserDetails userDetails = userService.loadUserByUsername(authenticatedUser.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);

        Map<String, Object> res = new HashMap<>();
        res.put("token", jwtToken);

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/register")
    public ResponseEntity<String> authenticate(@Valid @RequestBody RegisterUserDto registerUserDto){
        System.out.println("Registering user DTO: " + registerUserDto.getEmail());

        authService.signup(registerUserDto);
        return ResponseEntity.ok("User registered successfully. Please check your email for verification instructions.");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@Valid @RequestBody VerifyUserDto verifyUserDto) {
        authService.verifyUser(verifyUserDto);
        return ResponseEntity.ok("Account verified successfully");
        
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendVerificationCode(@RequestBody Map<String, String> body) {
        System.out.println("Resending verification code to email: " + body.get("email"));
        final String email = body.get("email"); 

        authService.resendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent");
    }

}
