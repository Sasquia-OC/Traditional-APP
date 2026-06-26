package test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TachesHandle implements HttpHandler {

    // CORRECTION : reçoit TacheService, pas Test
    private final Test service;

    public TachesHandle(Test service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        // Preflight CORS — OBLIGATOIRE pour DELETE/PUT
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            exchange.getResponseBody().close();
            return;
        }

        exchange.getResponseHeaders().add("Content-Type", "application/json");

        switch (exchange.getRequestMethod()) {
            case "GET" ->
                handleGet(exchange);
            case "POST" ->
                handlePost(exchange);
            case "PUT" ->
                handlePut(exchange);
            case "DELETE" ->
                handleDelete(exchange);
            default ->
                sendResponse(exchange, 405, "{}");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 200, service.toJson());
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        service.ajouterDepuisJson(body);
        sendResponse(exchange, 201, "{\"status\":\"ok\"}");
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        int id = parseIdFromQuery(exchange.getRequestURI().getQuery());
        String body = readBody(exchange);
        boolean ok = service.modifierDepuisJson(id, body);
        sendResponse(exchange, ok ? 200 : 404,
                ok ? "{\"status\":\"ok\"}" : "{\"error\":\"not found\"}");
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        int id = parseIdFromQuery(exchange.getRequestURI().getQuery());
        boolean ok = service.supprimer(id);
        sendResponse(exchange, ok ? 200 : 404,
                ok ? "{\"status\":\"ok\"}" : "{\"error\":\"not found\"}");
    }

    private String readBody(HttpExchange ex) throws IOException {
        try (java.io.InputStream in = ex.getRequestBody();
             java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1) {
                bout.write(buffer, 0, read);
            }
            return bout.toString(StandardCharsets.UTF_8.name());
        }
    }

    private void sendResponse(HttpExchange ex, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.getResponseBody().close();
    }

    private int parseIdFromQuery(String query) {
        if (query == null) {
            return -1;
        }
        return Integer.parseInt(query.replace("id=", ""));
    }
}
