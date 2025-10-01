package com.codewithmosh.store.auth;

import com.codewithmosh.store.users.User;
import com.codewithmosh.store.users.UserMapperImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserMapperImpl userMapperImpl;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = authService.handleLogin(loginRequest);
        response.addCookie(loginResponse.getCookie());
        return ResponseEntity.ok(new JwtResponse(loginResponse.getToken().toString()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") String refreshToken) {
        Jwt token = authService.handleRefresh(refreshToken);
        return ResponseEntity.ok(new JwtResponse(token.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        User user = authService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapperImpl.toDto(user));
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<Void> handleRefreshTokenInvalidException () {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
