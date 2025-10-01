package com.codewithmosh.store.auth;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Cookie cookie;
    private Jwt token;
}
