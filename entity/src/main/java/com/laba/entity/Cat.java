package com.laba.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Cat entity.
 */
@Entity
@Table(name = "cats")
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate birthday;
    private String breed;

    @Enumerated(EnumType.STRING)
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToMany
    @JoinTable(
            name = "cat_friends",
            joinColumns = @JoinColumn(name = "cat_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Cat> friends = new ArrayList<>();

    /**
     * Instantiates a new Cat.
     */
    public Cat() {}

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
    public Owner getOwner() { return owner; }

    /**
     * Sets owner.
     *
     * @param owner the owner
     */
    public void setOwner(Owner owner) { this.owner = owner; }

    /**
     * Gets friends.
     *
     * @return the friends
     */
    public List<Cat> getFriends() { return friends; }

    /**
     * Sets friends.
     *
     * @param friends the friends
     */
    public void setFriends(List<Cat> friends) { this.friends = friends != null ? friends : new ArrayList<>(); }

    /**
     * Add friend.
     *
     * @param friend the friend
     */
    public void addFriend(Cat friend) {
        if (friend != null && !friends.contains(friend) && friend != this) {
            friends.add(friend);
            if (!friend.getFriends().contains(this)) {
                friend.addFriend(this);
            }
        }
    }

    /**
     * Remove friend.
     *
     * @param friend the friend
     */
    public void removeFriend(Cat friend) {
        if (friend != null && friends.remove(friend)) {
            friend.getFriends().remove(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cat cat)) return false;
        return Objects.equals(id, cat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

//    @Override
//    public String toString() {
//        return "Cat{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", birthday=" + birthday +
//                ", breed='" + breed + '\'' +
//                ", color=" + color +
//                ", owner=" + (owner != null ? owner.getName() : "null") +
//                ", friends=" + friends.size() +
//                '}';
//    }

    /**
     * Is same boolean.
     *
     * @param other the other cat
     * @return the boolean
     */
    public boolean isSame(Cat other) {
        if (other == null) return false;
        return Objects.equals(name, other.name) &&
                Objects.equals(birthday, other.birthday) &&
                Objects.equals(breed, other.breed) &&
                color == other.color;
    }

    /**
     * Builder for cat.
     */
    public static class Builder {
        private String name;
        private LocalDate birthday;
        private String breed;
        private Color color;
        private Owner owner;
        private List<Cat> friends = new ArrayList<>();

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
         * Breed builder.
         *
         * @param breed the breed
         * @return the builder
         */
        public Builder breed(String breed) { this.breed = breed; return this; }

        /**
         * Color builder.
         *
         * @param color the color
         * @return the builder
         */
        public Builder color(Color color) { this.color = color; return this; }

        /**
         * Owner builder.
         *
         * @param owner the owner
         * @return the builder
         */
        public Builder owner(Owner owner) { this.owner = owner; return this; }

        /**
         * Friends builder.
         *
         * @param friends the friends
         * @return the builder
         */
        public Builder friends(List<Cat> friends) { this.friends = friends != null ? new ArrayList<>(friends) : new ArrayList<>(); return this; }

        /**
         * Add friend builder.
         *
         * @param friend the friend
         * @return the builder
         */
        public Builder addFriend(Cat friend) { if (friend != null && !friends.contains(friend)) friends.add(friend); return this; }

        /**
         * Build cat.
         *
         * @return the cat
         */
        public Cat build() {
            Cat cat = new Cat();
            cat.setName(name);
            cat.setBirthday(birthday);
            cat.setBreed(breed);
            cat.setColor(color);
            cat.setOwner(owner);
            cat.setFriends(friends);
            for (Cat friend : friends) {
                if (!friend.getFriends().contains(cat)) {
                    friend.addFriend(cat);
                }
            }
            return cat;
        }
    }
}