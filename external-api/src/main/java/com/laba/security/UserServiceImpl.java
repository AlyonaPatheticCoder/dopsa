package com.laba.security;

import com.laba.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl {

    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserClient userClient, PasswordEncoder passwordEncoder) {
        this.userClient = userClient;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto save(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getRole() == null) {
            userDto.setRole("USER");
        }
        return userClient.saveUser(userDto);
    }

    public List<UserDto> getAll() {
        return userClient.getAllUsers();
    }
}