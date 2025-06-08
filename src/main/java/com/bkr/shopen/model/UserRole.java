package com.bkr.shopen.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserRole {
    USER, ADMIN;

    @JsonCreator
    public static UserRole fromString(String value) {
        return UserRole.valueOf(value.trim().toUpperCase());
    }
}