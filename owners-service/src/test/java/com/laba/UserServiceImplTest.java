package com.laba;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.laba.dao.UserDao;
import com.laba.dto.UserDto;
import com.laba.entity.Role;
import com.laba.entity.User;
import com.laba.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;
    private UserServiceImpl userService;
    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDao);
    }

    @Test
    void save_successUser() {
        UserDto dto = new UserDto();
        dto.setUsername("user");
        dto.setPassword("pass");
        dto.setRole(Role.USER.name());
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("user");
        savedUser.setPassword("pass");
        savedUser.setRole(Role.USER);

        when(userDao.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.save(dto);

        assertEquals(1L, result.getId());
        assertEquals("user", result.getUsername());
        assertEquals(Role.USER.name(), result.getRole());
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void save_successAdmin() {
        UserDto dto = new UserDto();
        dto.setUsername("admin");
        dto.setPassword("pass");
        dto.setRole(Role.ADMIN.name());
        User savedUser = new User();
        savedUser.setId(2L);
        savedUser.setUsername("admin");
        savedUser.setPassword("pass");
        savedUser.setRole(Role.ADMIN);

        when(userDao.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.save(dto);
        assertEquals(2L, result.getId());
        assertEquals(Role.ADMIN.name(), result.getRole());
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void getAll_returnsList() {
        User u1 = new User();
        u1.setId(1L);
        u1.setUsername("u1");
        u1.setRole(Role.USER);
        User u2 = new User();
        u2.setId(2L);
        u2.setUsername("u2");
        u2.setRole(Role.USER);

        when(userDao.findAll()).thenReturn(List.of(u1, u2));
        List<UserDto> result = userService.getAll();
        assertEquals(2, result.size());
        assertEquals("u1", result.get(0).getUsername());
        assertEquals("u2", result.get(1).getUsername());
    }

    @Test
    void getById_returnsUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setRole(Role.USER);

        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        UserDto result = userService.getById(1L);
        assertEquals(1L, result.getId());
        assertEquals("user", result.getUsername());
    }

    @Test
    void getById_throwsException_notFound() {
        when(userDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.getById(1L));
    }

    @Test
    void update_success() {
        User existing = new User();
        existing.setId(1L);
        existing.setUsername("old_u");
        existing.setPassword("pass");
        existing.setRole(Role.USER);

        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setUsername("new_u");
        dto.setPassword("pass");
        dto.setRole(Role.ADMIN.name());

        User saved = new User();
        saved.setId(1L);
        saved.setUsername("new_u");
        saved.setPassword("pass");
        saved.setRole(Role.ADMIN);

        when(userDao.findById(1L)).thenReturn(Optional.of(existing));
        when(userDao.save(any(User.class))).thenReturn(saved);
        UserDto result = userService.update(dto);
        assertEquals(1L, result.getId());
        assertEquals("new_u", result.getUsername());
        assertEquals(Role.ADMIN.name(), result.getRole());
    }

    @Test
    void update_throwsException_userNotFound() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setUsername("new_u");
        when(userDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.update(dto));
    }

    @Test
    void delete_success() {
        User user = new User(); user.setId(1L);
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        userService.delete(1L);
        verify(userDao, times(1)).delete(user);
    }

    @Test
    void delete_throwsException_userNotFound() {
        when(userDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.delete(1L));
    }

    @Test
    void findByUserName_returnsUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setRole(Role.USER);
        when(userDao.findByUsername("user")).thenReturn(Optional.of(user));
        Optional<UserDto> result = userService.findByUserName("user");

        assertTrue(result.isPresent());
        assertEquals("user", result.get().getUsername());
    }

    @Test
    void findByUserName_returnsEmptyList() {
        when(userDao.findByUsername("user")).thenReturn(Optional.empty());
        Optional<UserDto> result = userService.findByUserName("user");
        assertTrue(result.isEmpty());
    }
}