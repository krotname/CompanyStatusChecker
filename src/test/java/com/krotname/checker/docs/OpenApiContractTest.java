package com.krotname.checker.docs;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("contract")
class OpenApiContractTest {
    private static final Path OPENAPI = Path.of("docs", "openapi.yaml");

    @Test
    void shouldDocumentPublicHttpContract() throws IOException {
        String yaml = Files.readString(OPENAPI);

        assertAll(
                () -> assertTrue(yaml.contains("openapi: 3.1.0")),
                () -> assertTrue(yaml.contains("  /health:")),
                () -> assertTrue(yaml.contains("  /api/check:")),
                () -> assertTrue(yaml.contains("        \"200\":")),
                () -> assertTrue(yaml.contains("        \"400\":")),
                () -> assertTrue(yaml.contains("        \"405\":")),
                () -> assertTrue(yaml.contains("                  status:")),
                () -> assertTrue(yaml.contains("                      - UNKNOWN")),
                () -> assertTrue(yaml.contains("                  dadataStatus:")),
                () -> assertTrue(yaml.contains("                    example: missing_inn"))
        );
    }
}
