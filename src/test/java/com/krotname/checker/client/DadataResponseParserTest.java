package com.krotname.checker.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"suggestions\":[\"not-an-object\"]}",
            "{\"suggestions\":[{\"data\":\"not-an-object\"}]}",
            "{\"suggestions\":[{\"data\":{\"state\":\"not-an-object\"}}]}",
            "{\"suggestions\":[{\"data\":{\"state\":{\"status\":{}}}}]}",
            "{\"suggestions\":[{\"data\":{\"state\":{\"status\":123}}}]}",
            "{\"suggestions\":[{\"data\":{\"state\":{\"status\":\"  \"}}}]}",
            "not-json"
    })
    void shouldReturnEmptyForUnexpectedPayloadShapes(String json) {
        assertTrue(parser.extractState(json).isEmpty());
    }
}
