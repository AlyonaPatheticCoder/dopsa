package com.laba.entity;

/**
 * Enum for cat colors.
 */
public enum Color {
    WHITE,
    BLACK,
    GINGER,
    GREY,
    BROWN,
    RAINBOW;

    /**
     * Checks if a given string matches any enum constant (case-insensitive).
     * @param value string value
     * @return corresponding Color or null if not found
     */
    public static Color fromString(String value) {
        if (value == null) return null;
        for (Color color : Color.values()) {
            if (color.name().equalsIgnoreCase(value)) {
                return color;
            }
        }
        return null;
    }
}