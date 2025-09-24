package com.laba.security;

import com.laba.client.UserClient;
import com.laba.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public UserDto update(UserDto userDto) {
        if (userDto.getPassword() != null) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        return userClient.updateUser(userDto);
    }

    public void delete(Long id) {
        userClient.deleteUser(id);
    }

    public UserDto getById(Long id) {
        return userClient.getUserById(id);
    }

    public List<UserDto> getAll() {
        return userClient.getAllUsers();
    }

    public Optional<UserDto> findByUserName(String username) {
        return userClient.findByUserName(username);
    }
}