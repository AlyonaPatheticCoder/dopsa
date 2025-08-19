package com.laba.validation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The type Validation.
 */
@Component
@ConfigurationProperties(prefix = "validation")
public class Validation{
    private final OwnerValidation ownerValidation = new OwnerValidation();
    private final CatValidation catValidation = new CatValidation();

    /**
     * Gets ownerValidation.
     *
     * @return the ownerValidation
     */
    public OwnerValidation getOwner() {
        return ownerValidation;
    }

    /**
     * Gets catValidation.
     *
     * @return the catValidation
     */
    public CatValidation getCat() {
        return catValidation;
    }

    /**
     * The type Owner validation.
     */
    public static class OwnerValidation {
        private int nameMaxLength;

        /**
         * Gets name max length.
         *
         * @return the name max length
         */
        public int getNameMaxLength() {
            return nameMaxLength;
        }

        /**
         * Sets name max length.
         *
         * @param nameMaxLength the name max length
         */
        public void setNameMaxLength(int nameMaxLength) {
            this.nameMaxLength = nameMaxLength;
        }
    }

    /**
     * The type Cat validation.
     */
    public static class CatValidation {
        private int nameMaxLength;
        private int breedMaxLength;

        /**
         * Gets name max length.
         *
         * @return the name max length
         */
        public int getNameMaxLength() {
            return nameMaxLength;
        }

        /**
         * Sets name max length.
         *
         * @param nameMaxLength the name max length
         */
        public void setNameMaxLength(int nameMaxLength) {
            this.nameMaxLength = nameMaxLength;
        }

        /**
         * Gets breed max length.
         *
         * @return the breed max length
         */
        public int getBreedMaxLength() {
            return breedMaxLength;
        }

        /**
         * Sets breed max length.
         *
         * @param breedMaxLength the breed max length
         */
        public void setBreedMaxLength(int breedMaxLength) {
            this.breedMaxLength = breedMaxLength;
        }
    }
}