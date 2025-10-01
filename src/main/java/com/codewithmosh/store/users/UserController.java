package com.codewithmosh.store.users;

import com.codewithmosh.store.common.ErrorDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "sort", required = false, defaultValue = "") String sort) {
        return userService.getUsers(sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterUserRequest userRequest) {
        return ResponseEntity.ok(userService.handleCreateUser(userRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest userRequest) {
        return ResponseEntity.ok(userService.handleUpdateUser(id, userRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changeUserPassword(@PathVariable Long id, @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.handleChangePassword(id, changePasswordRequest);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<ErrorDto> handleEmailDuplicateException(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<Void> handleWrongPasswordException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
