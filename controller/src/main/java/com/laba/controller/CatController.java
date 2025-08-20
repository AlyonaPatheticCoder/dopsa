package com.laba.controller;
import com.laba.dto.CatDto;
import com.laba.service.CatService;
import com.laba.validation.MaxLengthProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * Controller class for managing Cats.
 * Handles user requests to CatService.
 */
@RestController
@RequestMapping("/api/cats")
@Validated
public class CatController {

    private final CatService catService;

    /**
     * Instantiates a new Cat controller.
     *
     * @param catService the cat service
     */
    @Autowired
    public CatController(CatService catService) { this.catService = catService;}

    /**
     * Gets cat by id.
     *
     * @param id the id
     * @return the cat by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<CatDto> getCatById(@PathVariable @Positive Long id) {
        CatDto cat = catService.getCatById(id);
        if (cat == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(cat);
    }

    /**
     * Gets all cats.
     *
     * @return the all cats
     */
    @GetMapping
    public ResponseEntity<List<CatDto>> getAllCats() { return ResponseEntity.ok(catService.getAllCats());}

    /**
     * Find cats by name response entity.
     *
     * @param name the name
     * @return the response entity
     */
    @GetMapping("/search/by-name")
    public ResponseEntity<List<CatDto>> findCatsByName(
            @RequestParam @NotBlank @MaxLengthProperty(
                    property = "cat-name",
                    message = "Cat name must not exceed {max} characters"
            ) String name) {
        return ResponseEntity.ok(catService.findCatsByName(name));
    }

    /**
     * Find cats by owner name response entity.
     *
     * @param ownerName the owner name
     * @return the response entity
     */
    @GetMapping("/search/by-owner-name")
    public ResponseEntity<List<CatDto>> findCatsByOwnerName(
            @RequestParam @NotBlank @MaxLengthProperty(
                    property = "owner-name",
                    message = "Owner name must not exceed {max} characters"
            ) String ownerName) {
        return ResponseEntity.ok(catService.findCatsByOwnerName(ownerName));
    }

    /**
     * Find cats by owner id response entity.
     *
     * @param ownerId the owner id
     * @return the response entity
     */
    @GetMapping("/search/by-owner-id/{ownerId}")
    public ResponseEntity<List<CatDto>> findCatsByOwnerId(@PathVariable @Positive Long ownerId) {
        return ResponseEntity.ok(catService.findCatsByOwnerId(ownerId));
    }

    /**
     * Create cat response entity.
     *
     * @param catDto the cat dto
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<String> createCat(@RequestBody @Valid CatDto catDto) {
        catService.saveCat(catDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cat created successfully");
    }

    /**
     * Update cat response entity.
     *
     * @param id     the id
     * @param catDto the cat dto
     * @return the response entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCat(@PathVariable @Positive Long id,
                                            @RequestBody @Valid CatDto catDto) {
        catDto.setId(id);
        catService.updateCat(catDto);
        return ResponseEntity.ok("Cat updated successfully");
    }

    /**
     * Delete cat response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCat(@PathVariable @Positive Long id) {
        catService.deleteCat(id);
        return ResponseEntity.ok("Cat deleted successfully");
    }

    /**
     * Find cats by filter response entity.
     *
     * @param filter the filter
     * @return the response entity
     */
    @PostMapping("/search/filter")
    public ResponseEntity<List<CatDto>> findCatsByFilter(@RequestBody CatDto filter) {
        return ResponseEntity.ok(catService.findCatsByFilter(filter));
    }

    /**
     * Add friend response entity.
     *
     * @param catId    the cat id
     * @param friendId the friend id
     * @return the response entity
     */
    @PostMapping("/{catId}/friends/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable @Positive Long catId,
                                            @PathVariable @Positive Long friendId) {
        catService.addFriend(catId, friendId);
        return ResponseEntity.ok("Friend added successfully");
    }

    /**
     * Remove friend response entity.
     *
     * @param catId    the cat id
     * @param friendId the friend id
     * @return the response entity
     */
    @DeleteMapping("/{catId}/friends/{friendId}")
    public ResponseEntity<String> removeFriend(@PathVariable @Positive Long catId,
                                               @PathVariable @Positive Long friendId) {
        catService.removeFriend(catId, friendId);
        return ResponseEntity.ok("Friend removed successfully");
    }
}