package com.krotname.checker.client;

import com.krotname.checker.config.CorporateCheckerConfig;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpDadataClientTest {
    private HttpServer server;
    private int port;

    @BeforeEach
    void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/success", this::successResponse);
        server.createContext("/error", this::errorResponse);
        server.createContext("/bad", this::badJsonResponse);
        server.start();
        port = server.getAddress().getPort();
    }

    @AfterEach
    void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void shouldReturnStateForSuccessfulResponse() throws Exception {
        HttpDadataClient client = createClient("/success");
        Optional<String> state = client.fetchCompanyState("9710083390");
        assertTrue(state.isPresent());
        assertEquals("ACTIVE", state.get());
    }

    @Test
    void shouldRejectNon2xxResponses() throws Exception {
        HttpDadataClient client = createClient("/error");
        assertThrows(IOException.class, () -> client.fetchCompanyState("9710083390"));
    }

    @Test
    void shouldReturnEmptyWhenJsonDoesNotContainStatus() throws Exception {
        HttpDadataClient client = createClient("/bad");
        Optional<String> state = client.fetchCompanyState("9710083390");
        assertTrue(state.isEmpty());
    }

    private HttpDadataClient createClient(String path) throws Exception {
        CorporateCheckerConfig config = getConfigWithEndpoint("http://localhost:" + port + path);
        return new HttpDadataClient(config);
    }

    private CorporateCheckerConfig getConfigWithEndpoint(String endpoint) throws Exception {
        Constructor<CorporateCheckerConfig> constructor = CorporateCheckerConfig.class.getDeclaredConstructor(
                String.class,
                String.class,
                Duration.class
        );
        constructor.setAccessible(true);
        return constructor.newInstance("token", endpoint, Duration.ofSeconds(1));
    }

    private void successResponse(HttpExchange exchange) throws IOException {
        byte[] payload = """
                {"suggestions":[{"data":{"state":{"status":"ACTIVE"}}}]}
                """.getBytes(StandardCharsets.UTF_8);
        writeJson(exchange, 200, payload);
    }

    private void errorResponse(HttpExchange exchange) throws IOException {
        byte[] payload = "downstream error".getBytes(StandardCharsets.UTF_8);
        writeJson(exchange, 500, payload);
    }

    private void badJsonResponse(HttpExchange exchange) throws IOException {
        byte[] payload = "{\"unexpected\":true}".getBytes(StandardCharsets.UTF_8);
        writeJson(exchange, 200, payload);
    }

    private void writeJson(HttpExchange exchange, int statusCode, byte[] payload) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, payload.length);
        exchange.getResponseBody().write(payload);
        exchange.close();
    }
}

