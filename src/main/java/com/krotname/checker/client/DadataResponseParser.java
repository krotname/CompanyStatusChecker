package com.krotname.checker.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Optional;

/**
 * Parses DaData response payload and extracts the first legal entity state value.
 */
public final class DadataResponseParser {
    private static final String FIELD_SUGGESTIONS = "suggestions";
    private static final String FIELD_DATA = "data";
    private static final String FIELD_STATE = "state";
    private static final String FIELD_STATUS = "status";

    /**
     * Safely traverse DaData response structure and extract the company status field.
     * Returns empty optional when payload shape is unexpected or malformed to avoid
     * making network errors look like domain failures.
     */
    public Optional<String> extractState(String json) {
        try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonArray suggestions = root.getAsJsonArray(FIELD_SUGGESTIONS);
            if (suggestions == null || suggestions.isEmpty()) {
                return Optional.empty();
            }

            JsonElement firstSuggestion = suggestions.get(0);
            if (!firstSuggestion.isJsonObject()) {
                return Optional.empty();
            }

            JsonElement dataElement = firstSuggestion.getAsJsonObject().get(FIELD_DATA);
            if (dataElement == null || !dataElement.isJsonObject()) {
                return Optional.empty();
            }

            JsonElement stateElement = dataElement.getAsJsonObject().get(FIELD_STATE);
            if (stateElement == null || !stateElement.isJsonObject()) {
                return Optional.empty();
            }

            JsonElement statusElement = stateElement.getAsJsonObject().get(FIELD_STATUS);
            if (statusElement == null || !statusElement.isJsonPrimitive()
                    || !statusElement.getAsJsonPrimitive().isString()) {
                return Optional.empty();
            }

            String status = statusElement.getAsString();
            return status.isBlank() ? Optional.empty() : Optional.of(status);
        } catch (RuntimeException ignored) {
            return Optional.empty();
        }
    }
}
