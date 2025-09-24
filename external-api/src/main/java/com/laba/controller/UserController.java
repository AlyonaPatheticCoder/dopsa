package com.laba.controller;

import com.laba.dto.UserDto;
import com.laba.security.UserDetailsImpl;
import com.laba.security.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto userDto) {
        UserDto saved = userService.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<List<UserDto>> getAll(Authentication auth) {
        boolean isAdmin = isAdmin(auth);

        if (isAdmin) {
            return ResponseEntity.ok(userService.getAll());
        } else {
            Long userId = getUserId(auth);
            return ResponseEntity.ok(List.of(userService.getById(userId)));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<UserDto> getById(@PathVariable Long id, Authentication auth) {
        boolean isAdmin = isAdmin(auth);
        Long authUserId = getUserId(auth);

        if (!isAdmin && !id.equals(authUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/search/by-username")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<List<UserDto>> findByUsername(@RequestParam String username, Authentication auth) {
        boolean isAdmin = isAdmin(auth);
        Long authUserId = getUserId(auth);

        Optional<UserDto> userOpt = userService.findByUserName(username);
        List<UserDto> users = userOpt.map(List::of).orElseGet(List::of);

        if (!isAdmin) {
            users = users.stream()
                    .filter(u -> u.getId().equals(authUserId))
                    .toList();
        }

        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<UserDto> update(@PathVariable Long id,
                                          @Valid @RequestBody UserDto userDto,
                                          Authentication auth) {
        boolean isAdmin = isAdmin(auth);
        Long authUserId = getUserId(auth);

        if (!isAdmin && !id.equals(authUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        userDto.setId(id);
        UserDto updated = userService.update(userDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<String> delete(@PathVariable Long id, Authentication auth) {
        boolean isAdmin = isAdmin(auth);
        Long authUserId = getUserId(auth);

        if (!isAdmin && !id.equals(authUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        userService.delete(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private Long getUserId(Authentication auth) {
        if (auth.getPrincipal() instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) auth.getPrincipal()).getUser().getId();
        }
        throw new IllegalStateException("Unexpected principal type: " + auth.getPrincipal().getClass());
    }
}