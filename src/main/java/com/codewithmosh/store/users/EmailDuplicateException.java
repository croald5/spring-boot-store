package com.codewithmosh.store.users;

public class EmailDuplicateException extends RuntimeException {
    public EmailDuplicateException(String message) {
        super(message);
    }
}
