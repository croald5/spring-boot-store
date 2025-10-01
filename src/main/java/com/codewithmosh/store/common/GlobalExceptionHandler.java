package com.codewithmosh.store.common;

import com.codewithmosh.store.carts.CartNotFoundException;
import com.codewithmosh.store.orders.OrderNotFoundException;
import com.codewithmosh.store.products.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<HashMap<String, String>> handleValidationError(MethodArgumentNotValidException exception) {
        HashMap<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ErrorDto> handleNotReadableException() {
        return ResponseEntity.badRequest().body(new ErrorDto("Invalid request body!"));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    private ResponseEntity<ErrorDto> handleOrderNotFoundException() {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorDto("Order was not found!"));
    }

    @ExceptionHandler(CartNotFoundException.class)
    private ResponseEntity<ErrorDto> handleCartNotFoundException() {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorDto("Cart was not found!"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    private ResponseEntity<ErrorDto> handleProductNotFoundException() {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorDto("Product was not found!"));
    }
}
