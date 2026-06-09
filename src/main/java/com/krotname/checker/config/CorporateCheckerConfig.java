package com.krotname.checker.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

/**
 * Centralized runtime configuration for DaData integration.
 */
public final class CorporateCheckerConfig {

    public static final String DEFAULT_ENDPOINT = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/findById/party";
    private static final String DEFAULT_RESOURCE = "checker.properties";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

    private final String token;
    private final String endpoint;
    private final Duration timeout;

    private CorporateCheckerConfig(String token, String endpoint, Duration timeout) {
        this.token = token;
        this.endpoint = endpoint;
        this.timeout = timeout;
    }

    public static CorporateCheckerConfig fromEnvironmentOrResource() {
        // Resolve from explicit runtime settings first; fallback to resource only when explicitly absent.
        String token = resolveConfigValue("DADATA_TOKEN");
        String endpoint = resolveConfigValue("DADATA_ENDPOINT", DEFAULT_ENDPOINT);
        Duration timeout = DEFAULT_TIMEOUT;

        if ((token == null || token.isBlank()) && shouldLoadFromResource()) {
            token = loadFromResource();
        }

        if (token == null || token.isBlank()) {
            throw new IllegalStateException("DADATA_TOKEN is missing. Set env var DADATA_TOKEN or create resources/checker.properties.");
        }

        return new CorporateCheckerConfig(token.trim(), endpoint.trim(), timeout);
    }

    private static String resolveConfigValue(String key) {
        return resolveConfigValue(key, null);
    }

    private static String resolveConfigValue(String key, String defaultValue) {
        String propertyValue = System.getProperty(key);
        if (propertyValue != null) {
            return propertyValue;
        }
        String envValue = System.getenv(key);
        return envValue == null ? defaultValue : envValue;
    }

    public String token() {
        return token;
    }

    public String endpoint() {
        return endpoint;
    }

    public Duration timeout() {
        return timeout;
    }

    private static boolean shouldLoadFromResource() {
        return CorporateCheckerConfig.class.getClassLoader().getResource(DEFAULT_RESOURCE) != null;
    }

    private static String loadFromResource() {
        try (InputStream stream = CorporateCheckerConfig.class.getClassLoader().getResourceAsStream(DEFAULT_RESOURCE)) {
            if (stream == null) {
                return null;
            }
            Properties props = new Properties();
            props.load(stream);
            String token = props.getProperty("token");
            if (token != null && token.getBytes(StandardCharsets.UTF_8).length > 0) {
                return token;
            }
            return null;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read checker.properties", e);
        }
    }

    public static CorporateCheckerConfig fromToken(String token) {
        Objects.requireNonNull(token, "token");
        return new CorporateCheckerConfig(token, DEFAULT_ENDPOINT, DEFAULT_TIMEOUT);
    }
}
