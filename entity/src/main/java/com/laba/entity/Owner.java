package com.laba.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Owner entity.
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
     * Instantiates a new Owner.
     */
    public Owner() {}

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
     * Gets cats.
     *
     * @return the cats
     */
    public List<Cat> getCats() { return cats; }

    /**
     * Sets cats.
     *
     * @param cats the cats
     */
    public void setCats(List<Cat> cats) {
        this.cats = cats != null ? cats : new ArrayList<>();
        for (Cat cat : this.cats) {
            if (cat.getOwner() != this) {
                cat.setOwner(this);
            }
        }
    }

    /**
     * Add cat.
     *
     * @param cat the cat
     */
    public void addCat(Cat cat) {
        if (cat != null && !cats.contains(cat)) {
            cats.add(cat);
            cat.setOwner(this);
        }
    }

    /**
     * Remove cat.
     *
     * @param cat the cat
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
        return "Owner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", cats=" + cats.size() +
                '}';
    }

    /**
     * Is same boolean.
     *
     * @param other the other owner
     * @return the boolean
     */
    public boolean isSame(Owner other) {
        if (other == null) return false;
        return Objects.equals(name, other.name) &&
                Objects.equals(birthday, other.birthday);
    }

    /**
     * Builder for Owner.
     */
    public static class Builder {
        private String name;
        private LocalDate birthday;
        private List<Cat> cats = new ArrayList<>();

        /**
         * Name builder.
         *
         * @param name the name
         * @return the builder
         */
        public Builder name(String name) { this.name = name; return this; }

        /**
         * Birthday builder.
         *
         * @param birthday the birthday
         * @return the builder
         */
        public Builder birthday(LocalDate birthday) { this.birthday = birthday; return this; }

        /**
         * Cats builder.
         *
         * @param cats the cats
         * @return the builder
         */
        public Builder cats(List<Cat> cats) { this.cats = cats != null ? new ArrayList<>(cats) : new ArrayList<>(); return this; }

        /**
         * Add cat builder.
         *
         * @param cat the cat
         * @return the builder
         */
        public Builder addCat(Cat cat) { if (cat != null && !cats.contains(cat)) cats.add(cat); return this; }

        /**
         * Build owner.
         *
         * @return the owner
         */
        public Owner build() {
            Owner owner = new Owner();
            owner.setName(name);
            owner.setBirthday(birthday);
            owner.setCats(cats);
            for (Cat cat : cats) {
                if (cat.getOwner() != owner) {
                    cat.setOwner(owner);
                }
            }
            return owner;
        }
    }
}