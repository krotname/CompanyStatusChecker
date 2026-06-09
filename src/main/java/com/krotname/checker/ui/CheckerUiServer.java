package com.krotname.checker.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.krotname.checker.CheckerCorporate;
import com.krotname.checker.model.CheckResult;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public final class CheckerUiServer {
    private static final String INDEX_RESOURCE = "static/index.html";
    private final CheckerCorporate checker;
    private final int requestedPort;
    private final Gson gson;
    private HttpServer server;

    public CheckerUiServer(CheckerCorporate checker, int requestedPort) {
        this.checker = checker;
        this.requestedPort = requestedPort;
        this.gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    }

    public int start() throws IOException {
        if (server != null) {
            return getPort();
        }
        server = HttpServer.create(new InetSocketAddress(requestedPort), 0);
        server.createContext("/", this::handleIndex);
        server.createContext("/api/check", this::handleCheck);
        server.createContext("/health", this::handleHealth);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();
        return getPort();
    }

    public int getPort() {
        Objects.requireNonNull(server, "server");
        return server.getAddress().getPort();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private void handleIndex(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            writeStatus(exchange, 405, "Method Not Allowed");
            return;
        }
        byte[] payload = loadIndexPage();
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, payload.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(payload);
        }
    }

    private void handleHealth(HttpExchange exchange) throws IOException {
        writeJson(exchange, 200, "{\"status\":\"ok\"}");
    }

    /**
     * Handles /api/check endpoint and delegates domain validation to CheckerCorporate.
     * Missing query params are returned as a 400 payload, all other logic stays in one place.
     */
    private void handleCheck(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            writeStatus(exchange, 405, "Method Not Allowed");
            return;
        }
        URI uri = exchange.getRequestURI();
        Map<String, String> query = queryParams(uri.getRawQuery());
        String inn = query.getOrDefault("inn", "").trim();
        if (inn.isEmpty()) {
            writeJson(exchange, 400, gson.toJson(Map.of("error", "missing_inn")));
            return;
        }
        CheckResult result = checker.check(inn);
        writeJson(exchange, 200, gson.toJson(result));
    }

    /**
     * Parses simple query strings used by the UI without introducing framework
     * dependencies; keeps endpoint resilient to malformed parameters.
     */
    private Map<String, String> queryParams(String rawQuery) {
        if (rawQuery == null || rawQuery.isBlank()) {
            return Map.of();
        }
        return java.util.Arrays.stream(rawQuery.split("&"))
                .map(entry -> entry.split("=", 2))
                .filter(parts -> parts.length == 2 && !decode(parts[0]).isBlank())
                .collect(Collectors.toMap(
                        parts -> decode(parts[0]),
                        parts -> decode(parts[1]),
                        (existing, replacement) -> existing,
                        java.util.LinkedHashMap::new
                ));
    }

    private void writeStatus(HttpExchange exchange, int statusCode, String message) throws IOException {
        writeJson(exchange, statusCode, gson.toJson(Map.of("error", message)));
    }

    private void writeJson(HttpExchange exchange, int statusCode, String body) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        byte[] payload = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, payload.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(payload);
        }
    }

    private byte[] loadIndexPage() throws IOException {
        // Keep the endpoint resilient: fallback page prevents hard 500 if static resource is removed.
        try (InputStream in = CheckerUiServer.class.getClassLoader().getResourceAsStream(INDEX_RESOURCE)) {
            if (in == null) {
                return "<h1>Checker UI is unavailable</h1>".getBytes(StandardCharsets.UTF_8);
            }
            return in.readAllBytes();
        }
    }

    private String decode(String value) {
        try {
            return java.net.URLDecoder.decode(value, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return value;
        }
    }

}
