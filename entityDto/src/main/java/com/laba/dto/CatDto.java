package com.laba.dto;

import com.laba.entity.Cat;
import com.laba.entity.Color;
import com.laba.entity.Owner;
import com.laba.validation.MaxLengthProperty;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 *Cat DTO.
 */
public class CatDto {

    private Long id;

    @NotBlank(message = "Cat name must not be blank")
    /*@MaxLengthProperty(
            property = "validation.max-length.cat-name",
            message = "Cat name must not exceed {max} characters"
    )*/

    @Size(min=1, max=10)
    private String name;

    @PastOrPresent(message = "Birthday cannot be in the future")
    private LocalDate birthday;

    @NotBlank(message = "Breed must not be blank")
    @Size(min=1, max=10)
    private String breed;

    @NotNull(message = "Color must not be null")
    private Color color;

    private Long ownerId;
    private OwnerDto owner;
    private List<FriendDto> friends = new ArrayList<>();

    /**
     * Friend DTO.
     */
    public static class FriendDto {
        private Long id;

        @NotBlank(message = "Friend name must not be blank")
        @MaxLengthProperty(
                property = "cat-name",
                message = "Friend must not exceed {max} characters"
        )
        private String name;

        /**
         * Instantiates a new Friend dto.
         */
        public FriendDto() {}

        /**
         * Instantiates a new Friend dto.
         *
         * @param id   the id
         * @param name the name
         */
        public FriendDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        /**
         * From entity friend dto.
         *
         * @param cat the cat
         * @return the friend dto
         */
        public static FriendDto fromEntity(com.laba.entity.Cat cat) {
            return new FriendDto(cat.getId(), cat.getName());
        }

        /**
         * To entity cat dto.
         *
         * @return cat
         */
        public Cat toEntity() {
            Cat cat = new Cat();
            cat.setId(this.id);
            cat.setName(this.name);
            return cat;
        }

        /**
         * Gets id.
         *
         * @return the id
         */
        public Long getId() { return id; }

        /**
         * Sets id.
         *
         * @param id the id
         */
        public void setId(Long id) { this.id = id; }

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() { return name; }

        /**
         * Sets name.
         *
         * @param name the name
         */
        public void setName(String name) { this.name = name; }
    }

    /**
     * From entity cat dto.
     *
     * @param cat the cat
     * @return the cat dto
     */
    public static CatDto fromEntity(com.laba.entity.Cat cat) {
        CatDto dto = new CatDto();
        dto.setId(cat.getId());
        dto.setName(cat.getName());
        dto.setBirthday(cat.getBirthday());
        dto.setBreed(cat.getBreed());
        dto.setColor(cat.getColor());

        if (cat.getOwner() != null) {
            dto.setOwnerId(cat.getOwner().getId());
            dto.setOwner(OwnerDto.fromEntity(cat.getOwner()));
        }

        dto.setFriends(Optional.ofNullable(cat.getFriends())
                .orElse(Collections.emptyList())
                .stream()
                .map(FriendDto::fromEntity)
                .collect(Collectors.toList()));

        return dto;
    }

    /**
     * To entity Cat.
     *
     * @return cat.
     */
    public Cat toEntity() {
        Cat cat = new Cat();
        cat.setId(this.id);
        cat.setName(this.name);
        cat.setBirthday(this.birthday);
        cat.setBreed(this.breed);
        cat.setColor(this.color);

        if (this.owner != null) {
            cat.setOwner(this.owner.toEntity());
        } else if (this.ownerId != null) {
            Owner owner = new Owner();
            owner.setId(this.ownerId);
            cat.setOwner(owner);
        }

        if (this.friends != null) {
            cat.setFriends(
                    this.friends.stream()
                            .map(FriendDto::toEntity)
                            .collect(Collectors.toList())
            );
        }
        return cat;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() { return id; }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets birthday.
     *
     * @return the birthday
     */
    public LocalDate getBirthday() { return birthday; }

    /**
     * Sets birthday.
     *
     * @param birthday the birthday
     */
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    /**
     * Gets breed.
     *
     * @return the breed
     */
    public String getBreed() { return breed; }

    /**
     * Sets breed.
     *
     * @param breed the breed
     */
    public void setBreed(String breed) { this.breed = breed; }

    /**
     * Gets color.
     *
     * @return the color
     */
    public Color getColor() { return color; }

    /**
     * Sets color.
     *
     * @param color the color
     */
    public void setColor(Color color) { this.color = color; }

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public OwnerDto getOwner() { return owner; }

    /**
     * Sets owner.
     *
     * @param owner the owner
     */
    public void setOwner(OwnerDto owner) { this.owner = owner; }

    /**
     * Gets owner id.
     *
     * @return the owner id
     */
    public Long getOwnerId() { return ownerId; }

    /**
     * Sets owner id.
     *
     * @param ownerId the owner id
     */
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    /**
     * Gets friends.
     *
     * @return the friends
     */
    public List<FriendDto> getFriends() { return friends; }

    /**
     * Sets friends.
     *
     * @param friends the friends
     */
    public void setFriends(List<FriendDto> friends) { this.friends = friends; }
}