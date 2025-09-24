package com.laba.service;

import com.laba.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto save(UserDto userDto);
    List<UserDto> getAll();
    UserDto getById(Long id);
    UserDto update(UserDto userDto);
    void delete(Long id);
    Optional<UserDto> findByUserName(String username);
}
