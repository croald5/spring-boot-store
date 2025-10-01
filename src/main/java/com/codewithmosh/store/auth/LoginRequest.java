package com.codewithmosh.store.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull(message = "Email must not be null")
    @Email
    private String email;
    @NotNull(message = "Password must not be null")
    private String password;
}
