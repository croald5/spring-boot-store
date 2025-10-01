package com.codewithmosh.store.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.crypto.SecretKey;
import java.util.Date;

@Data
@AllArgsConstructor
public class Jwt {
    private final Claims claims;
    private final SecretKey key;

    public boolean isValid() {
        return claims.getExpiration().after(new Date());

    }

    public Long getUserId() {
        return Long.valueOf(claims.getSubject());
    }

    public String getRole() {
        return claims.get("role", String.class);
    }

    public String toString() {
        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }
}
