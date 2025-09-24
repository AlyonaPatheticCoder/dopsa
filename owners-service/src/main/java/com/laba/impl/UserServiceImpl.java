package com.laba.impl;
import com.laba.dao.UserDao;
import com.laba.dto.UserDto;
import com.laba.entity.Role;
import com.laba.entity.User;
import com.laba.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    @Override
    public UserDto save(UserDto userDto) {
        User user = userDto.toEntity();
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        User savedUser = userDao.save(user);
        return UserDto.fromEntity(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return userDao.findAll().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        return userDao.findById(id)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) {
        User existing = userDao.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        existing.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            existing.setPassword(userDto.getPassword());
        }
        existing.setRole(userDto.getRole() != null ? Role.valueOf(userDto.getRole()) : existing.getRole());
        User savedUser = userDao.save(existing);
        return UserDto.fromEntity(savedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userDao.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findByUserName(String username) {
        return userDao.findByUsername(username)
                .map(UserDto::fromEntity);
    }
}