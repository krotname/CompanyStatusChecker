package com.krotname.checker.client;

import com.krotname.checker.config.CorporateCheckerConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

import static java.net.http.HttpResponse.BodyHandlers.ofString;

public final class HttpDadataClient implements DadataClient {
    private final HttpClient httpClient;
    private final CorporateCheckerConfig config;
    private final DadataResponseParser parser;

    public HttpDadataClient(CorporateCheckerConfig config) {
        this(HttpClient.newBuilder().build(), config, new DadataResponseParser());
    }

    HttpDadataClient(HttpClient httpClient, CorporateCheckerConfig config, DadataResponseParser parser) {
        this.httpClient = httpClient;
        this.config = config;
        this.parser = parser;
    }

    /**
     * Calls DaData and returns the first found company state. Non-2xx responses are
     * treated as integration errors and converted into {@link IOException} to keep
     * domain logic error handling centralized.
     */
    @Override
    public Optional<String> fetchCompanyState(String inn) throws IOException, InterruptedException {
        String payload = String.format("{\"query\":\"%s\",\"count\":1}", inn);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(config.endpoint()))
                .timeout(config.timeout())
                .header("Authorization", "Token " + config.token())
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, ofString(StandardCharsets.UTF_8));
        int statusCode = response.statusCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException("Dadata returned status code " + statusCode);
        }
        return parser.extractState(response.body());
    }
}
