package com.codewithmosh.store.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Size( max = 255, message = "Name must be less than 255 characters")
    @Lowercase(message = "Name must be lowercase")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 25, message = "Password must be between 8 and 25 characters")
    private String password;
}
