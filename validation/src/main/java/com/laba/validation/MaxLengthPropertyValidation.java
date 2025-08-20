package com.laba.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;

/**
 * Max length property validation.
 */
@Component
public class MaxLengthPropertyValidation implements ConstraintValidator<MaxLengthProperty, String> {

    private final Validation validation;
    private String field;
    private int maxLength;

    /**
     * Instantiates a new Max length property validation.
     *
     * @param validation the validation
     */
    public MaxLengthPropertyValidation(Validation validation) {
        this.validation = validation;
    }

    @Override
    public void initialize(MaxLengthProperty constraintAnnotation) {
        this.field = constraintAnnotation.property();

        switch (field) {
            case "owner-name":
                maxLength = validation.getOwner().getName();
                break;
            case "cat-name":
                maxLength = validation.getCat().getName();
                break;
            case "breed":
                maxLength = validation.getCat().getBreed();
                break;
            default:
                throw new IllegalArgumentException("Unknown property: " + field);
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        boolean valid = value.length() <= maxLength;

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    context.getDefaultConstraintMessageTemplate().replace("{max}", String.valueOf(maxLength))
            ).addConstraintViolation();
        }

        return valid;
    }
}