package com.laba.impl;

import com.laba.dao.UserDao;
import com.laba.dto.UserDto;
import com.laba.entity.Role;
import com.laba.entity.User;
import com.laba.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = userDto.toEntity();
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        User saved = userDao.save(user);
        return UserDto.fromEntity(saved);
    }

    @Override
    public List<UserDto> getAll() {
        return userDao.findAll().stream()
                .map(UserDto::fromEntity)
                .toList();
    }
}