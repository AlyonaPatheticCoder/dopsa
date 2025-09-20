package com.laba.controller;

import com.laba.dto.UserDto;
import com.laba.security.UserClient;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserClient userClient;

    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto userDto) {
        UserDto savedUser = userClient.saveUser(userDto);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> users = userClient.getAllUsers();
        return ResponseEntity.ok(users);
    }
}