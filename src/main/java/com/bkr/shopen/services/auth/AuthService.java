package com.bkr.shopen.services.auth;

import com.bkr.shopen.dto.LoginUserDto;
import com.bkr.shopen.dto.RegisterUserDto;
import com.bkr.shopen.dto.VerifyUserDto;
import com.bkr.shopen.error.BadRequestExceptionErr;
import com.bkr.shopen.error.ResourceNotFoundExceptionErr;
import com.bkr.shopen.model.User;
import com.bkr.shopen.repository.UserRepository;
import jakarta.mail.MessagingException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User signup(RegisterUserDto userDto) {
        User user = new User(userDto.getUsername(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()));

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
            String code = generateVerificationCode();
            String hashed = hashCode(code);

            user.setVerificationCode(hashed); 
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
            userRepository.save(user);

            sendVerificationEmail(user.getEmail(), code); 
            
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
    }

    public User authenticate(LoginUserDto input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new ResourceNotFoundExceptionErr("User not found"));

        // if (!user.isEmailVerified()) {
        //     throw new RuntimeException("Account not verified. Please verify your account.");
        // }
        if (!user.isEnabled()) {
            throw new RuntimeException("Account suspended. Please verify your account.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return user;
    }

    public ResponseEntity<String> verifyUser(VerifyUserDto input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());

        if(!optionalUser.isPresent()) {
             throw new ResourceNotFoundExceptionErr("User not found");
        }

        User user = optionalUser.get();

        if (user.isVerified()) {
            throw new RuntimeException("Account is already verified");
        }

        if (isUserVerificationCodeValid(user, input.getVerificationCode())) {
            throw new BadRequestExceptionErr("Invalid verification code");
        } 

        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);
        return ResponseEntity.ok("Account verified successfully");
       
    }

    private void sendVerificationEmail(String email, String plainCode) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + plainCode;
        
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(email, subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send verification email: " + e.getMessage(), e);
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(!optionalUser.isPresent()) {
            throw new RuntimeException("User not found with email: " + email);
        }

        User user = optionalUser.get();
        
        if (user.isEnabled()) {
            throw new RuntimeException("Account is already verified");
        }

        String code = generateVerificationCode();
        String hashed = hashCode(code);

        user.setVerificationCode(hashed);
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));

        
        sendVerificationEmail(email, code);
     
        userRepository.save(user);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    private boolean isUserVerificationCodeValid(User user, String inputCode) {
        if (user.getVerificationCode() == null || user.getVerificationCode().isBlank()) {
            throw new RuntimeException("User verification code is not set");
        }
        if (user.getVerificationCodeExpiresAt() == null || user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code has expired");
        }   

        System.out.println("User verification code: " + user.getVerificationCode());
        System.out.println("Input verification code: " + inputCode);
        return true;
    }

    private String hashCode(String code) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(code.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Error hashing verification code", e);
    }
}
}