package com.bkr.shopen.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUserDto {

    private final String username;

    @Email(message = "Invalid email format")
    private final String email;

    @NotBlank(message = "Password is required")
    private final String password;
}
