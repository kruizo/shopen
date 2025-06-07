package com.bkr.shopen.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class VerifyUserDto {

    @Email(message = "Invalid email format")
    private final String email;

    private final String verificationCode;
}
