package com.krotname.checker;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Minimal Docker healthcheck entry point.
 * It avoids curl/wget dependencies in the runtime image and checks the same HTTP
 * health endpoint that CI smoke tests use.
 */
public final class ContainerHealthCheck {
    static final String DEFAULT_ENDPOINT = "http://127.0.0.1:8080/health";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(2);
    private static final String HEALTHY_PAYLOAD = "\"status\":\"ok\"";

    private ContainerHealthCheck() {
    }

    public static void main(String[] args) throws IOException {
        String endpoint = args.length == 0 ? DEFAULT_ENDPOINT : args[0];
        if (!isHealthy(endpoint, DEFAULT_TIMEOUT)) {
            throw new IOException("Health endpoint is not ready: " + endpoint);
        }
    }

    static boolean isHealthy(String endpoint, Duration timeout) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(timeout)
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .timeout(timeout)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return response.statusCode() == 200 && response.body().contains(HEALTHY_PAYLOAD);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (IOException | IllegalArgumentException e) {
            return false;
        }
    }
}
