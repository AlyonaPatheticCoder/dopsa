package com.laba.entity;

public enum Role {
    ADMIN,
    USER;

    /**
     * Checks if a given string matches any enum constant (case-insensitive).
     * @param value string value
     * @return corresponding Role or null if not found
     */
    public static Role fromString(String value) {
        if (value == null) return null;
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        return null;
    }
}