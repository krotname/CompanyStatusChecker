package com.krotname.checker.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class InnValidatorTest {
    private final InnValidator validator = new InnValidator();

    @Test
    void shouldAcceptKnownValidInns() {
        assertTrue(validator.isValid("9710083390"));
        assertTrue(validator.isValid("7604147344"));
    }

    @Test
    void shouldRejectInvalidShape() {
        assertFalse(validator.isValid(null));
        assertFalse(validator.isValid(""));
        assertFalse(validator.isValid("abc"));
        assertFalse(validator.isValid("123"));
        assertFalse(validator.isValid("97100833901"));
    }
}
