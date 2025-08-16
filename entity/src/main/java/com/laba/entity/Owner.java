package com.laba.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an Owner entity.
 */
@Entity
@Table(name = "owners")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate birthday;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Cat> cats = new ArrayList<>();


    /**
     * Default constructor.
     */
    public Owner() {}


    /**
     * Builder class for creating {@link Owner} instances.
     */
    public static class Builder {
        private String name;
        private LocalDate birthday;
        private List<Cat> cats = new ArrayList<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder birthday(LocalDate birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder cats(List<Cat> cats) {
            this.cats = cats != null ? new ArrayList<>(cats) : new ArrayList<>();
            return this;
        }

        /**
         * Adds a single cat to owner.
         *
         * @param cat the Cat to add
         * @return this Builder instance
         */
        public Builder addCat(Cat cat) {
            if (cat != null && !this.cats.contains(cat)) {
                this.cats.add(cat);
                cat.setOwner(null); // Owner will be set in build() to avoid overwriting
            }
            return this;
        }

        /**
         * Builds a new {@link Owner} instance.
         *
         * @return Owner object
         */
        public Owner build() {
            Owner owner = new Owner();
            owner.setName(this.name);
            owner.setBirthday(this.birthday);
            owner.setCats(this.cats);

            for (Cat cat : this.cats) {
                if (cat.getOwner() != owner) {
                    cat.setOwner(owner);
                }
            }

            return owner;
        }
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets birthday.
     *
     * @return the birthday
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Gets cats.
     *
     * @return the cats
     */
    public List<Cat> getCats() {
        return cats;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets birthday.
     *
     * @param birthday the birthday
     */
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    /**
     * Sets cats.
     *
     * @param cats the cats
     */
    public void setCats(List<Cat> cats) {
        this.cats = cats != null ? cats : new ArrayList<>();
    }

    /**
     * Adds a cat to the owner.
     * @param cat the Cat to add
     */
    public void addCat(Cat cat) {
        if (cat != null && !cats.contains(cat)) {
            cats.add(cat);
            cat.setOwner(this);
        }
    }

    /**
     * Removes a cat from the owner .
     * @param cat the Cat to remove
     */
    public void removeCat(Cat cat) {
        if (cat != null && cats.remove(cat)) {
            cat.setOwner(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Owner owner)) return false;
        return Objects.equals(id, owner.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Owner:\n" + "id: " + id + "\n" + "name: " + name + "\n" + "birthday: " + birthday + "\n" + "cats: " + cats;
    }

    /**
     * Checks if another owner has the same fields.
     * @param other another Owner
     * @return true if name and birthday match
     */
    public boolean isSame(Owner other) {
        if (other == null) return false;
        return Objects.equals(name, other.name) && Objects.equals(birthday, other.birthday);
    }
}