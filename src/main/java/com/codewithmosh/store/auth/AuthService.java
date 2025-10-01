package com.codewithmosh.store.auth;

import com.codewithmosh.store.users.User;
import com.codewithmosh.store.users.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final UserRepository userRepository;

  public User getCurrentUser() {
      var authentication = SecurityContextHolder.getContext().getAuthentication();
      Long userId = (Long) authentication.getPrincipal();
      return userRepository.findById(userId).orElse(null);
  }

  public LoginResponse handleLogin(LoginRequest request) {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
      User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
      Jwt token = jwtService.generateAccessToken(user);
      Jwt refreshToken = jwtService.generateRefreshToken(user);
      Cookie cookie = new Cookie("refreshToken", refreshToken.toString());
      cookie.setHttpOnly(true);
      cookie.setPath("/auth/refresh");
      cookie.setMaxAge(604800);
      cookie.setSecure(true);
      return new LoginResponse(cookie, token);
  }

    public Jwt handleRefresh(String refreshToken) {
        var jwt = jwtService.parse(refreshToken);
        if (jwt == null || !jwt.isValid()) {
            throw new RefreshTokenInvalidException();
        }
        User user = userRepository.findById(jwt.getUserId()).orElseThrow();
        return jwtService.generateAccessToken(user);
    }
}
