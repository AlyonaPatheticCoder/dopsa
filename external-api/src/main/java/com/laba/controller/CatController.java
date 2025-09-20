package com.laba.controller;

import com.laba.dto.CatDto;
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
    public ResponseEntity<CatDto> getCatById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(catClient.getCatById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<CatDto>> getAllCats() {
        return ResponseEntity.ok(catClient.getAllCats());
    }

    @GetMapping("/search/by-name")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<CatDto>> findCatsByName(@RequestParam @NotBlank String name) {
        return ResponseEntity.ok(catClient.findCatsByName(name));
    }

    @GetMapping("/search/by-owner-id/{ownerId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<CatDto>> findCatsByOwnerId(@PathVariable @Positive Long ownerId) {
        return ResponseEntity.ok(catClient.findCatsByOwnerId(ownerId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createCat(@Valid @RequestBody CatDto catDto) {
        catClient.saveCat(catDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cat created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateCat(@PathVariable @Positive Long id,
                                            @Valid @RequestBody CatDto catDto) {
        catDto.setId(id);
        catClient.updateCat(catDto);
        return ResponseEntity.ok("Cat updated successfully");
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