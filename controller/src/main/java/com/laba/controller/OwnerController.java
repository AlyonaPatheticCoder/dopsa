package com.laba.controller;
import com.laba.dto.OwnerDto;
import com.laba.service.OwnerService;
import com.laba.validation.MaxLengthProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing Owners.
 * Handles user requests to OwnerService.
 */
@RestController
@RequestMapping("/api/owners")
@Validated
public class OwnerController {

    private final OwnerService ownerService;

    /**
     * Instantiates a new Owner controller.
     *
     * @param ownerService the owner service
     */
    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    /**
     * Gets owner by id.
     *
     * @param id the id
     * @return the owner by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> getOwnerById(@PathVariable @Positive Long id) {
        OwnerDto owner = ownerService.getOwnerById(id);
        if (owner == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(owner);
    }

    /**
     * Gets all owners.
     *
     * @return the all owners
     */
    @GetMapping
    public ResponseEntity<List<OwnerDto>> getAllOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    /**
     * Find owners by name response entity.
     *
     * @param name the name
     * @return the response entity
     */
    @GetMapping("/search/by-name")
    public ResponseEntity<List<OwnerDto>> findOwnersByName(
            @RequestParam @NotBlank @MaxLengthProperty(
                    property = "owner-name",
                    message = "Owner name must not exceed {max} characters"
            ) String name) {
        return ResponseEntity.ok(ownerService.findOwnersByName(name));
    }

    /**
     * Find owners by cat name response entity.
     *
     * @param catName the cat name
     * @return the response entity
     */
    @GetMapping("/search/by-cat-name")
    public ResponseEntity<List<OwnerDto>> findOwnersByCatName(
            @RequestParam("catName") @NotBlank @MaxLengthProperty(
                    property = "cat-name",
                    message = "Cat name must not exceed {max} characters"
            ) String catName) {
        return ResponseEntity.ok(ownerService.findOwnersByCatName(catName));
    }

    /**
     * Find owner by cat id response entity.
     *
     * @param catId the cat id
     * @return the response entity
     */
    @GetMapping("/search/by-cat-id/{catId}")
    public ResponseEntity<OwnerDto> findOwnerByCatId(@PathVariable @Positive Long catId) {
        return ResponseEntity.ok(ownerService.findOwnerByCatId(catId));
    }

    /**
     * Create owner response entity.
     *
     * @param ownerDto the owner dto
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<String> createOwner(@Valid @RequestBody OwnerDto ownerDto) {
        ownerService.saveOwner(ownerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Owner created successfully");
    }

    /**
     * Update owner response entity.
     *
     * @param id       the id
     * @param ownerDto the owner dto
     * @return the response entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateOwner(@PathVariable @Positive Long id,
                                              @Valid @RequestBody OwnerDto ownerDto) {
        ownerDto.setId(id);
        ownerService.updateOwner(ownerDto);
        return ResponseEntity.ok("Owner updated successfully");
    }

    /**
     * Delete owner response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOwner(@PathVariable @Positive Long id) {
        ownerService.deleteOwner(id);
        return ResponseEntity.ok("Owner deleted successfully");
    }

    /**
     * Find owners by filter response entity.
     *
     * @param filter the filter
     * @return the response entity
     */
    @PostMapping("/search/filter")
    public ResponseEntity<List<OwnerDto>> findOwnersByFilter(@RequestBody OwnerDto filter) {
        return ResponseEntity.ok(ownerService.findOwnersByFilter(filter));
    }

    /**
     * Add cat to owner response entity.
     *
     * @param ownerId the owner id
     * @param catId   the cat id
     * @return the response entity
     */
    @PostMapping("/{ownerId}/cats/{catId}")
    public ResponseEntity<String> addCatToOwner(@PathVariable @Positive Long ownerId, @PathVariable @Positive Long catId) {
        ownerService.addCatToOwner(ownerId, catId);
        return ResponseEntity.ok("Cat added to owner successfully");
    }

    /**
     * Remove cat from owner response entity.
     *
     * @param ownerId the owner id
     * @param catId   the cat id
     * @return the response entity
     */
    @DeleteMapping("/{ownerId}/cats/{catId}")
    public ResponseEntity<String> removeCatFromOwner(@PathVariable @Positive Long ownerId, @PathVariable @Positive Long catId) {
        ownerService.removeCatFromOwner(ownerId, catId);
        return ResponseEntity.ok("Cat removed from owner successfully");
    }
}