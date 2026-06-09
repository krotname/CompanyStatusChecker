package com.krotname.checker.validation;

/**
 * Performs defensive INN checks:
 * 1) basic format (10 or 12 digits);
 * 2) optional check-sum validation for the most common Russian legal entity rules.
 *
 * The checksum pass is intentionally strict to avoid network calls with malformed identifiers.
 */
public class InnValidator {
    private static final int[] WEIGHTS_10 = {2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int[] WEIGHTS_12_FIRST = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int[] WEIGHTS_12_SECOND = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

    public boolean isValid(String inn) {
        if (inn == null) {
            return false;
        }
        String normalized = inn.trim();
        if (!normalized.matches("\\d{10}") && !normalized.matches("\\d{12}")) {
            return false;
        }

        int[] digits = normalized.chars().map(Character::getNumericValue).toArray();
        if (normalized.length() == 10) {
            return validateChecksum(digits, WEIGHTS_10, 9);
        }
        int first = validateChecksum(digits, WEIGHTS_12_FIRST, 10) ? 1 : 0;
        int second = validateChecksum(digits, WEIGHTS_12_SECOND, 11) ? 1 : 0;
        return first == 1 && second == 1;
    }

    private boolean validateChecksum(int[] digits, int[] weights, int controlIndex) {
        // Weighted checksum is the core INN guardrail; any mismatch blocks external API calls early.
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += digits[i] * weights[i];
        }
        int control = sum % 11;
        if (control > 9) {
            control %= 10;
        }
        return digits[controlIndex] == control;
    }
}
