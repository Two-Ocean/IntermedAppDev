package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Difficulty {
    EASY, MEDIUM, HARD;

    @JsonCreator
    public static Difficulty fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Difficulty value cannot be null or empty");
        }
        try {
            return Difficulty.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid difficulty value: " + value);
        }
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}
