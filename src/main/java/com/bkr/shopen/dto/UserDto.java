package com.bkr.shopen.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private final Integer id;
    private final String username;
    private final String email;
    private final boolean isVerified;
    private final boolean enabled;
    private final Instant createdAt;
    private final Instant updatedAt;
  

}
