package nullscape.mike.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.repository.ImageRepository;
import nullscape.mike.repository.ItemRepository;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ImageController implements HttpHandler {

    private Map<String, String> parsePathParams(String path, String pattern) {
        Map<String, String> pathParams = new HashMap<>();

        String[] patternParts = pattern.split("/");
        String[] pathParts = path.split("/");

        if (patternParts.length != pathParts.length) {
            return pathParams;
        }

        for (int i = 0; i < patternParts.length; i++) {
            if (patternParts[i].startsWith("{") && patternParts[i].endsWith("}")) {
                String key = patternParts[i].substring(1, patternParts[i].length() - 1);
                pathParams.put(key, pathParts[i]);
            }
        }
        return pathParams;
    }

    // this should get the parameters passed in through the url e.g. items?id=123
    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryPairs = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return queryPairs;
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                queryPairs.put(key, value);
            }
        }
        return queryPairs;
    }

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            Map<String, String> params = parsePathParams(exchange.getRequestURI().getPath(), "/api/images/{filename}");
            String filename = params.get("filename");
            String filePath = "uploads/" + filename;
            File file = new File(filePath);

            if (!file.exists()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            byte[] bytes = Files.readAllBytes(file.toPath());

            // optional: basic content type
            exchange.getResponseHeaders().add("Content-Type", "image/jpeg");

            exchange.sendResponseHeaders(200, bytes.length);

            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }

        else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery()); //items?id=123
            ItemRepository.addImage(params.get("id"), ImageRepository.addImage(exchange.getRequestBody()));

            exchange.sendResponseHeaders(200, -1);
        }
    }
}