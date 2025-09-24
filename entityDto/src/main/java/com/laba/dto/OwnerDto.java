package com.laba.dto;
import com.laba.validation.MaxLengthProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import com.laba.entity.Cat;
import com.laba.entity.Owner;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Owner DTO.
 */
public class OwnerDto {
    private Long id;

    @NotBlank(message = "Owner name must not be blank")
    @Size(min=1, max=10)
    private String name;

    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be in the past")
    private LocalDate birthday;

    private List<Long> catIds = new ArrayList<>();
    private List<String> catNames = new ArrayList<>();

    /**
     * Instantiates a new Owner dto.
     */
    public OwnerDto() {}

    /**
     * From entity owner dto.
     *
     * @param owner the owner
     * @return the owner dto
     */
    public static OwnerDto fromEntity(Owner owner) {
        OwnerDto dto = new OwnerDto();
        dto.setId(owner.getId());
        dto.setName(owner.getName());
        dto.setBirthday(owner.getBirthday());

        if (owner.getCats() != null) {
            dto.setCatIds(owner.getCats().stream()
                    .map(Cat::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));

            dto.setCatNames(owner.getCats().stream()
                    .map(Cat::getName)
                    .filter(Objects::nonNull)
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * To entity owner.
     *
     * @return the owner
     */
    public Owner toEntity() {
        Owner owner = new Owner();
        owner.setId(this.id);
        owner.setName(this.name);
        owner.setBirthday(this.birthday);

        if (this.catIds != null && !this.catIds.isEmpty()) {
            List<Cat> cats = this.catIds.stream().map(id -> {
                Cat c = new Cat();
                c.setId(id);
                c.setOwner(owner);
                return c;
            }).collect(Collectors.toList());
            owner.setCats(cats);
        }

        return owner;
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
     * Gets cat ids.
     *
     * @return the cat ids
     */
    public List<Long> getCatIds() { return catIds; }

    /**
     * Sets cat ids.
     *
     * @param catIds the cat ids
     */
    public void setCatIds(List<Long> catIds) { this.catIds = catIds; }

    /**
     * Gets cat names.
     *
     * @return the cat names
     */
    public List<String> getCatNames() { return catNames; }

    /**
     * Sets cat names.
     *
     * @param catNames the cat names
     */
    public void setCatNames(List<String> catNames) { this.catNames = catNames; }
}