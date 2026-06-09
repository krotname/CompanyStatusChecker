package com.krotname.checker.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unit")
@Tag("contract")
class DadataResponseParserTest {
    private final DadataResponseParser parser = new DadataResponseParser();

    @Test
    void shouldExtractActiveStateFromResponse() {
        String json = """
                {"suggestions":[{"data":{"state":{"status":"ACTIVE"}}}]}
                """;
        Optional<String> state = parser.extractState(json);
        assertTrue(state.isPresent());
        assertEquals("ACTIVE", state.get());
    }

    @Test
    void shouldReturnEmptyOnMissingSuggestion() {
        String json = """
                {"suggestions":[]}
                """;
        assertTrue(parser.extractState(json).isEmpty());
    }
}
