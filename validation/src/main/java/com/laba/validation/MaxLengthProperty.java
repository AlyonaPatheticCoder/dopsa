package com.laba.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * The interface Max length property.
 */
@Documented
@Constraint(validatedBy = MaxLengthPropertyValidation.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxLengthProperty {
    /**
     * Message string.
     *
     * @return the string
     */
    String message() default "Field length exceeds maximum allowed";

    /**
     * Property string.
     *
     * @return the string
     */
    String property();

    /**
     * Groups class [ ].
     *
     * @return the class [ ]
     */
    Class<?>[] groups() default {};

    /**
     * Payload class [ ].
     *
     * @return the class [ ]
     */
    Class<? extends Payload>[] payload() default {};
}