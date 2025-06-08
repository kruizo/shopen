package com.bkr.shopen.services.auth;

import com.bkr.shopen.dto.LoginUserDto;
import com.bkr.shopen.dto.RegisterUserDto;
import com.bkr.shopen.dto.UserDto;
import com.bkr.shopen.dto.VerifyUserDto;
import com.bkr.shopen.error.BadRequestExceptionErr;
import com.bkr.shopen.error.ConflictExceptionErr;
import com.bkr.shopen.error.InternalServerExceptionErr;
import com.bkr.shopen.error.ResourceNotFoundExceptionErr;
import com.bkr.shopen.mapper.UserMapper;
import com.bkr.shopen.model.User;
import com.bkr.shopen.repository.UserRepository;
import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Value("${security.jwt.private-key-path}")
    private Resource privateKeyResource;

    @Value("${security.jwt.public-key-path}")
    private Resource publicKeyResource;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Transactional
    public ResponseEntity<String> signup(RegisterUserDto userDto) {
        if(userDto.getPassword() == null || userDto.getPassword().isBlank()) {
            throw new BadRequestExceptionErr("Password cannot be blank");
        }

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new BadRequestExceptionErr("User with this username already exists");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BadRequestExceptionErr("User with this email already exists");
        }

        User user = new User(userDto.getUsername(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()));

        String code = generateVerificationCode();
        String hashed = hashCode(code);

        user.setVerificationCode(hashed); 
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        sendVerificationEmail(user.getEmail(), code); 

        try {
            userRepository.save(user);
            System.out.println("User verification code Before: " + code);
            return ResponseEntity.ok("User registered successfully. Please check your email for verification code.");
        } catch (DataIntegrityViolationException e) {
            throw new ConflictExceptionErr("User already exists or violates database constraints", e);
        } catch (Exception e) {
            throw new InternalServerExceptionErr("Unexpected error saving user", e);
        }
    }


    public UserDto authenticate(LoginUserDto input) {

        User user = userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account suspended. Please verify your account.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user .getUsername(),
                        input.getPassword()
                )
        );

        return UserMapper.toDto(user);
    }



    @Transactional
    public void verifyUser(VerifyUserDto input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());

        if(!optionalUser.isPresent()) {
             throw new ResourceNotFoundExceptionErr("User not found");
        }

        User user = optionalUser.get();

        if (user.isVerified()) {
            throw new BadRequestExceptionErr("Account is already verified");
        }

        if (!isUserVerificationCodeValid(user, input.getVerificationCode())) {
            throw new BadRequestExceptionErr("Invalid verification code");
        } 

        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);

        try {
             userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictExceptionErr("User already exists or violates database constraints", e);
        } catch (Exception e) {
            throw new InternalServerExceptionErr("Unexpected error saving user", e);
        }
       
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
            throw new InternalServerExceptionErr("Failed to send verification email: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (email == null || email.isBlank()) {
            throw new BadRequestExceptionErr("Email cannot be null or blank");
        }

        if(!optionalUser.isPresent()) {
            throw new ResourceNotFoundExceptionErr("User not found.");
        }

        User user = optionalUser.get();
        
        if (user.isVerified()) {
            throw new BadRequestExceptionErr("Account is already verified");
        }

        String code = generateVerificationCode();
        String hashed = hashCode(code);

        user.setVerificationCode(hashed);
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));

        sendVerificationEmail(email, code);
        
        try {
             userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictExceptionErr("Action violates database constraints", e);
        } catch (Exception e) {
            throw new InternalServerExceptionErr("Unexpected error saving user", e);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    private boolean isUserVerificationCodeValid(User user, String inputCode) {
        if (user.getVerificationCode() == null || user.getVerificationCode().isBlank()) {
            throw new BadRequestExceptionErr("User verification code is not set");
        }

        if (user.getVerificationCodeExpiresAt() == null || user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestExceptionErr("Verification code has expired");
        }   

        String hashedInputCode = hashCode(inputCode);

        return user.getVerificationCode().equals(hashedInputCode);

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

    private Optional<User> findUserByUsernameOrEmail(String username, String email) {
        if (email != null && !email.isBlank()) {
            return userRepository.findByEmail(email);
        }
        if (username != null && !username.isBlank()) {
            return userRepository.findByUsername(username);
        }
        return Optional.empty();
    }

}