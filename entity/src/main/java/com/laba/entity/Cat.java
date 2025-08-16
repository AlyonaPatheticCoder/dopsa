package com.laba.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Cat entity.
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


    /** Default constructor. */
    public Cat() {}

    /**
     * Builder class for creating {@link Cat} instances.
     */
    public static class Builder {
        private String name;
        private LocalDate birthday;
        private String breed;
        private Color color;
        private Owner owner;
        private List<Cat> friends = new ArrayList<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder birthday(LocalDate birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder breed(String breed) {
            this.breed = breed;
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder owner(Owner owner) {
            this.owner = owner;
            return this;
        }

        public Builder friends(List<Cat> friends) {
            this.friends = friends != null ? new ArrayList<>(friends) : new ArrayList<>();
            return this;
        }

        /**
         * Adds a single friend to cat.
         *
         * @param friend a Cat to add
         * @return this Builder instance
         */
        public Builder addFriend(Cat friend) {
            if ((friend != null) && !this.friends.contains(friend)) {
                this.friends.add(friend);
            }
            return this;
        }

        /**
         * Builds a new {@link Cat} instance.
         *
         * @return Cat object
         */
        public Cat build() {
            Cat cat = new Cat();
            cat.setName(this.name);
            cat.setBirthday(this.birthday);
            cat.setBreed(this.breed);
            cat.setColor(this.color);
            cat.setOwner(this.owner);
            cat.setFriends(this.friends);

            for (Cat friend : this.friends) {
                if (!friend.getFriends().contains(cat)) {
                    friend.addFriend(cat);
                }
            }

            return cat;
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
     * Gets breed.
     *
     * @return the breed
     */
    public String getBreed() {
        return breed;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * Gets friends.
     *
     * @return the friends
     */
    public List<Cat> getFriends() {
        return friends;
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
     * Sets breed.
     *
     * @param breed the breed
     */
    public void setBreed(String breed) {
        this.breed = breed;
    }

    /**
     * Sets color.
     *
     * @param color the color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets owner.
     *
     * @param owner the owner
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * Sets friends.
     *
     * @param friends the friends
     */
    public void setFriends(List<Cat> friends) {
        this.friends = friends != null ? friends : new ArrayList<>();
    }

    /**
     * Adds a friend and ensures bidirectional friendship.
     * @param friend friend
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
     * Removes a friend from this cat and ensures bidirectional removal.
     * @param friend another Cat to remove
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

    @Override
    public String toString() {
        return "Cat:\n" + "id: " + id + "\n" + "name: " + name + "\n" + ", birthday: " + birthday + "\n" + "breed: " + breed + "\n" +
                "color: " + color + "\n" + "owner name: " + (owner != null ? owner.getName() : "null") + "\n" + "friends: " + friends;
    }

    /**
     * Checks if another cat has the same basic attributes (ignoring id and owner).
     * @param other another Cat
     * @return true if name, birthday, breed, and color match
     */
    public boolean isSame(Cat other) {
        if (other == null) return false;
        return Objects.equals(name, other.name) &&
                Objects.equals(birthday, other.birthday) &&
                Objects.equals(breed, other.breed) &&
                color == other.color;
    }
}