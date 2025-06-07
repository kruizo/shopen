package com.bkr.shopen.mapper;

import com.bkr.shopen.dto.UserDto;
import com.bkr.shopen.model.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.isVerified(),
            user.isEnabled(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}