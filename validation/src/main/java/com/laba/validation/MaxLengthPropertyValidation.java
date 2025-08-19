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

    private final Environment env;
    private String propertyKey;
    private int maxLength;

    /**
     * Instantiates a new Max length property validation.
     *
     * @param env the env
     */
    public MaxLengthPropertyValidation(Environment env) {
        this.env = env;
    }

    /**
     * Initializer
     *
     * @param constraintAnnotation constraintAnnotation
     */
    @Override
    public void initialize(MaxLengthProperty constraintAnnotation) {
        this.propertyKey = constraintAnnotation.property();
        String maxLengthStr = env.getProperty(propertyKey);
        if (maxLengthStr != null) {
            this.maxLength = Integer.parseInt(maxLengthStr);
        }
    }

    /**
     * Validates string max lenght
     *
     * @param value string length
     * @param context constraint validator context
     * @return valid boolean
     */
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