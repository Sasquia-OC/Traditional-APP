package test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticHandler implements HttpHandler {

    private final String webRoot;

    public StaticHandler(String webRoot) {
        this.webRoot = webRoot;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String uriPath = exchange.getRequestURI().getPath();

        // "/" redirige vers index.html
        if (uriPath.equals("/")) {
            uriPath = "/index.html";
        }

        File file = new File(webRoot + uriPath);

        // Fichier introuvable → 404
        if (!file.exists()) {
            String msg = "404 - Fichier non trouve";
            exchange.sendResponseHeaders(404, msg.length());
            exchange.getResponseBody().write(msg.getBytes());
            exchange.getResponseBody().close();
            return;
        }

        // Détecte le bon type selon l'extension
        String ext = uriPath.substring(uriPath.lastIndexOf("."));
        String contentType = "application/octet-stream";
        switch (ext) {
            case ".html":
                contentType = "text/html; charset=UTF-8";
                break;
            case ".css":
                contentType = "text/css";
                break;
            case ".js":
                contentType = "application/javascript";
                break;
        }

        byte[] bytes = Files.readAllBytes(file.toPath());
        exchange.getResponseHeaders().add("Content-Type", contentType);
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }
}