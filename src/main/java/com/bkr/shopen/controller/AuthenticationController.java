package com.bkr.shopen.controller;
import com.bkr.shopen.services.auth.AuthService;
import com.bkr.shopen.services.auth.JwtService;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bkr.shopen.dto.LoginUserDto;
import com.bkr.shopen.dto.RegisterUserDto;
import com.bkr.shopen.dto.VerifyUserDto;
import com.bkr.shopen.model.User;
import com.bkr.shopen.response.LoginResponse;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/auth/")
public class AuthenticationController {

    private final AuthService authService;
    private final JwtService jwtService;
    
    /**
     * Constructor for RegistrationController.
     *
     * @param RegistrationService the service for user registration
     * @param jwtService the service for JWT token generation
     */

    public AuthenticationController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @GetMapping("/user")
    public ResponseEntity<String> registerPage() {
        return ResponseEntity.ok("On register page with method called with param: ");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginUserDto loginUserDto){
        User authenticatedUser = authService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<String> authenticate(@Valid @RequestBody RegisterUserDto registerUserDto){
        System.out.println("Registering user DTO: " + registerUserDto.getEmail());

       return ResponseEntity.ok("User registered successfully.");
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
