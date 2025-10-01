package com.codewithmosh.store.users;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import static java.util.Objects.*;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getUsers(String sort) {
        if (!Set.of("name", "email").contains(sort)) {
            sort = "name";
        }
        return userRepository.findAll(Sort.by(sort).descending()).stream().map(userMapper::toDto).toList();
    }

    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (isNull(user)) {
            throw new UserNotFoundException();
        }
        return userMapper.toDto(user);
    }

    public UserDto handleCreateUser(RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new EmailDuplicateException("Email is already registered");
        }
        User user = userMapper.toEntity(registerUserRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto handleUpdateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElse(null);
        if (isNull(user)) {
            throw new UserNotFoundException();
        }
        userMapper.toUpdate(request, user);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public void handleDeleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (isNull(user)) {
            throw new UserNotFoundException();
        }
        userRepository.delete(user);
    }

    public void handleChangePassword(Long id, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(id).orElse(null);
        if (isNull(user)) {
            throw new UserNotFoundException();
        }
        if (!user.getPassword().equals(changePasswordRequest.getOldPassword())) {
            throw new WrongPasswordException();
        }
        user.setPassword(changePasswordRequest.getNewPassword());
        userRepository.save(user);
    }
}
