package com.laba.dto;

import com.laba.entity.Owner;
import com.laba.entity.Role;
import com.laba.entity.User;
import com.laba.validation.MaxLengthProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDto {

    private Long id;

    @NotBlank(message = "Username must not be blank")
    @MaxLengthProperty(
            property = "validation.max-length.username",
            message = "Username must not exceed {max} characters"
    )
    private String username;

    @NotBlank(message = "Password must not be blank")
    @MaxLengthProperty(
            property = "validation.max-length.password",
            message = "Password must not exceed {max} characters"
    )
    private String password;

    @NotNull(message = "Role must not be null")
    private Role role;

    private Long ownerId;
    private OwnerDto owner;

    /**
     * Instantiates a new User dto.
     */
    public UserDto() {}

    /**
     * From entity user dto.
     *
     * @param user the user
     * @return the user dto
     */
    public static UserDto fromEntity(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());

        if (user.getOwner() != null) {
            dto.setOwnerId(user.getOwner().getId());
            dto.setOwner(OwnerDto.fromEntity(user.getOwner()));
        }
        return dto;
    }

    /**
     * To entity user.
     *
     * @return user
     */
    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setRole(this.role);

        if (this.owner != null) {
            user.setOwner(this.owner.toEntity());
        } else if (this.ownerId != null) {
            Owner owner = new Owner();
            owner.setId(this.ownerId);
            user.setOwner(owner);
        }
        return user;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public Long getOwnerId() { return ownerId; }

    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public OwnerDto getOwner() { return owner; }

    public void setOwner(OwnerDto owner) { this.owner = owner; }
}