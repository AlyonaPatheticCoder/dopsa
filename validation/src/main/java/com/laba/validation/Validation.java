package com.laba.validation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The type Validation.
 */
@Component
@ConfigurationProperties(prefix = "validation")
public class Validation {

    private OwnerValidation owner = new OwnerValidation();
    private CatValidation cat = new CatValidation();
    private UserValidation user = new UserValidation();

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public OwnerValidation getOwner() {
        return owner;
    }

    /**
     * Sets owner.
     *
     * @param ownerValidation the owner validation
     */
    public void setOwner(OwnerValidation ownerValidation) {
        this.owner = ownerValidation;
    }

    /**
     * Gets cat.
     *
     * @return the cat
     */
    public CatValidation getCat() {
        return cat;
    }

    /**
     * Sets cat.
     *
     * @param catValidation the cat validation
     */
    public void setCat(CatValidation catValidation) {
        this.cat = catValidation;
    }

    public UserValidation getUser() {return user; }
    public void setUser(UserValidation userValidation) { this.user = userValidation; }

    /**
     * The type Owner validation.
     */
    public static class OwnerValidation {
        private int name;

        /**
         * Gets name.
         *
         * @return the name
         */
        public int getName() {
            return name;
        }

        /**
         * Sets name.
         *
         * @param name the name
         */
        public void setName(int name) {
            this.name = name;
        }
    }

    /**
     * The type Cat validation.
     */
    public static class CatValidation {
        private int name;
        private int breed;

        /**
         * Gets name.
         *
         * @return the name
         */
        public int getName() {
            return name;
        }

        /**
         * Sets name.
         *
         * @param name the name
         */
        public void setName(int name) {
            this.name = name;
        }

        /**
         * Gets breed.
         *
         * @return the breed
         */
        public int getBreed() {
            return breed;
        }

        /**
         * Sets breed.
         *
         * @param breed the breed
         */
        public void setBreed(int breed) {
            this.breed = breed;
        }
    }

    public static class UserValidation {
        private int username;

        /**
         * Gets name.
         *
         * @return username
         */
        public int getUsername() {
            return username;
        }

        /**
         * Sets name.
         *
         * @param name the name
         */
        public void setUsername(int name) {
            this.username = name;
        }
    }
}