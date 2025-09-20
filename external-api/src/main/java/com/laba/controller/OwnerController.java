package com.laba.controller;
import com.laba.dto.OwnerDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<OwnerDto> getOwnerById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(ownerClient.getOwnerById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<OwnerDto>> getAllOwners() {
        return ResponseEntity.ok(ownerClient.getAllOwners());
    }

    @GetMapping("/search/by-name")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<OwnerDto>> findOwnersByName(
            @RequestParam @NotBlank String name) {
        return ResponseEntity.ok(ownerClient.findOwnersByName(name));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createOwner(@Valid @RequestBody OwnerDto ownerDto) {
        ownerClient.saveOwner(ownerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Owner created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateOwner(@PathVariable @Positive Long id,
                                              @Valid @RequestBody OwnerDto ownerDto) {
        ownerDto.setId(id);
        ownerClient.updateOwner(ownerDto);
        return ResponseEntity.ok("Owner updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOwner(@PathVariable @Positive Long id) {
        ownerClient.deleteOwner(id);
        return ResponseEntity.ok("Owner deleted successfully");
    }

    @PostMapping("/search/filter")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<OwnerDto>> findOwnersByFilter(@RequestBody OwnerDto filter) {
        return ResponseEntity.ok(ownerClient.findOwnersByFilter(filter));
    }

    @PostMapping("/{ownerId}/cats/{catId}")
    public ResponseEntity<String> addCatToOwner(@PathVariable Long ownerId,
                                                @PathVariable Long catId) {
        catClient.setOwner(catId, ownerId);
        ownerClient.addCatToOwner(ownerId, catId);
        return ResponseEntity.ok("Cat added to owner successfully");
    }

    @DeleteMapping("/{ownerId}/cats/{catId}")
    public ResponseEntity<String> removeCatFromOwner(@PathVariable Long ownerId,
                                                     @PathVariable Long catId) {
        catClient.removeOwner(catId);
        ownerClient.removeCatFromOwner(ownerId, catId);
        return ResponseEntity.ok("Cat removed from owner successfully");
    }
}