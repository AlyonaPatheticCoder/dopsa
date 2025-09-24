package com.laba.controller;

import com.laba.client.CatClient;
import com.laba.dto.CatDto;
import com.laba.security.UserDetailsImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing Cats.
 * Handles user requests to CatService.
 */
@RestController
@RequestMapping("/api/v1/cats")
@Validated
public class CatController {

    private final CatClient catClient;

    public CatController(CatClient catClient) {
        this.catClient = catClient;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<CatDto> getCatById(@PathVariable @Positive Long id, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        CatDto cat = catClient.getCatById(id);

        if (!isAdmin) {
            Long userOwnerId = ((UserDetailsImpl) auth.getPrincipal()).getUser().getOwnerId();
            if (!cat.getOwnerId().equals(userOwnerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(cat);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<CatDto>> getAllCats(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return ResponseEntity.ok(catClient.getAllCats());
        } else {
            Long userOwnerId = ((UserDetailsImpl) auth.getPrincipal()).getUser().getOwnerId();
            return ResponseEntity.ok(catClient.findCatsByOwnerId(userOwnerId));
        }
    }

    @GetMapping("/search/by-name")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<CatDto>> findCatsByName(@RequestParam @NotBlank String name, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        List<CatDto> cats = catClient.findCatsByName(name);

        if (!isAdmin) {
            Long userOwnerId = ((UserDetailsImpl) auth.getPrincipal()).getUser().getOwnerId();
            cats = cats.stream()
                    .filter(c -> c.getOwnerId().equals(userOwnerId))
                    .toList();
        }

        return ResponseEntity.ok(cats);
    }

    @GetMapping("/search/by-owner-id/{ownerId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<CatDto>> findCatsByOwnerId(@PathVariable @Positive Long ownerId, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            Long userOwnerId = ((UserDetailsImpl) auth.getPrincipal()).getUser().getOwnerId();
            if (!ownerId.equals(userOwnerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.ok(catClient.findCatsByOwnerId(ownerId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CatDto> createCat(@Valid @RequestBody CatDto catDto) {
        CatDto saved = catClient.saveCat(catDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CatDto> updateCat(@PathVariable @Positive Long id,
                                            @Valid @RequestBody CatDto catDto) {
        catDto.setId(id);
        CatDto updated = catClient.updateCat(catDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCat(@PathVariable @Positive Long id) {
        catClient.deleteCat(id);
        return ResponseEntity.ok("Cat deleted successfully");
    }

    @PostMapping("/{catId}/friends/{friendId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addFriend(@PathVariable @Positive Long catId,
                                            @PathVariable @Positive Long friendId) {
        catClient.addFriend(catId, friendId);
        return ResponseEntity.ok("Friend added successfully");
    }

    @DeleteMapping("/{catId}/friends/{friendId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeFriend(@PathVariable @Positive Long catId,
                                               @PathVariable @Positive Long friendId) {
        catClient.removeFriend(catId, friendId);
        return ResponseEntity.ok("Friend removed successfully");
    }
}