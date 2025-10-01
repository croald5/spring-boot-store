package com.codewithmosh.store.auth;

import com.codewithmosh.store.users.User;
import com.codewithmosh.store.users.UserMapper;
import com.codewithmosh.store.users.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;

    public Jwt generateAccessToken(User user) {
        final long tokenExpiration = jwtConfig.getAccessTokenExpiration(); //5m
        return generateToken(user, tokenExpiration);
    }

    public Jwt generateRefreshToken(User user) {
        final long tokenExpiration = jwtConfig.getRefreshTokenExpiration(); //7d
        return generateToken(user, tokenExpiration);
    }

    public Jwt parse(String token) {
        try {
            var claims = Jwts.parser().verifyWith(jwtConfig.getSecretKey()).build().parseSignedClaims(token).getPayload();
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException ex) {
            return null;
        }
    }

    private Jwt generateToken(User user, long tokenExpiration) {
        Claims claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("name", user.getName())
                .add("email", user.getEmail())
                .add("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .build();
        return new Jwt(claims, jwtConfig.getSecretKey());
    }
}
