package com.laba.controller;
import com.laba.client.CatClient;
import com.laba.client.OwnerClient;
import com.laba.dto.OwnerDto;
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
 * Controller class for managing Owners.
 * Handles user requests to OwnerService.
 */
@RestController
@RequestMapping("/api/v1/owners")
@Validated
public class OwnerController {

    private final OwnerClient ownerClient;
    private final CatClient catClient;

    public OwnerController(OwnerClient ownerClient, CatClient catClient) {
        this.ownerClient = ownerClient;
        this.catClient = catClient;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<OwnerDto> getOwnerById(@PathVariable @Positive Long id, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Long userOwnerId = ((UserDetailsImpl) auth.getPrincipal()).getUser().getOwnerId();
        if (!isAdmin && !id.equals(userOwnerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(ownerClient.getOwnerById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<OwnerDto>> getAllOwners(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return ResponseEntity.ok(ownerClient.getAllOwners());
        } else {
            Long userOwnerId = ((UserDetailsImpl) auth.getPrincipal()).getUser().getOwnerId();
            return ResponseEntity.ok(List.of(ownerClient.getOwnerById(userOwnerId)));
        }
    }

    @GetMapping("/search/by-name")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<OwnerDto>> findOwnersByName(@RequestParam @NotBlank String name, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<OwnerDto> owners = ownerClient.findOwnersByName(name);
        if (!isAdmin) {
            Long userOwnerId = ((UserDetailsImpl) auth.getPrincipal()).getUser().getOwnerId();
            owners = owners.stream()
                    .filter(o -> o.getId().equals(userOwnerId))
                    .toList();
        }

        return ResponseEntity.ok(owners);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OwnerDto> createOwner(@Valid @RequestBody OwnerDto ownerDto) {
        OwnerDto saved = ownerClient.saveOwner(ownerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OwnerDto> updateOwner(@PathVariable @Positive Long id,
                                                @Valid @RequestBody OwnerDto ownerDto) {
        ownerDto.setId(id);
        OwnerDto updated = ownerClient.updateOwner(ownerDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOwner(@PathVariable @Positive Long id) {
        ownerClient.deleteOwner(id);
        return ResponseEntity.ok("Owner deleted successfully");
    }

    @PostMapping("/search/filter")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<OwnerDto>> findOwnersByFilter(@RequestBody OwnerDto filter, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<OwnerDto> owners = ownerClient.findOwnersByFilter(filter);
        if (!isAdmin) {
            Long userOwnerId = ((UserDetailsImpl) auth.getPrincipal()).getUser().getOwnerId();
            owners = owners.stream()
                    .filter(o -> o.getId().equals(userOwnerId))
                    .toList();
        }

        return ResponseEntity.ok(owners);
    }

    @PostMapping("/{ownerId}/cats/{catId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<String> addCatToOwner(@PathVariable Long ownerId,
                                                @PathVariable Long catId,
                                                Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Long userOwnerId = ((UserDetailsImpl) auth.getPrincipal()).getUser().getOwnerId();
        if (!isAdmin && !ownerId.equals(userOwnerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        catClient.setOwner(catId, ownerId);
        ownerClient.addCatToOwner(ownerId, catId);
        return ResponseEntity.ok("Cat added to owner successfully");
    }

    @DeleteMapping("/{ownerId}/cats/{catId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<String> removeCatFromOwner(@PathVariable Long ownerId,
                                                     @PathVariable Long catId,
                                                     Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Long userOwnerId = ((UserDetailsImpl) auth.getPrincipal()).getUser().getOwnerId();
        if (!isAdmin && !ownerId.equals(userOwnerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        catClient.removeOwner(catId);
        ownerClient.removeCatFromOwner(ownerId, catId);
        return ResponseEntity.ok("Cat removed from owner successfully");
    }
}