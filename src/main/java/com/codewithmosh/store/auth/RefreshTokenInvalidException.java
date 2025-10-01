package com.codewithmosh.store.auth;

public class RefreshTokenInvalidException extends RuntimeException {
    public RefreshTokenInvalidException() {
        super("Refresh token is invalid");
    }
}
