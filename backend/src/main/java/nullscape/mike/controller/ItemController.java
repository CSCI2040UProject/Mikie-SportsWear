package nullscape.mike.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.model.Item;
import nullscape.mike.repository.ItemRepository;
import nullscape.mike.service.SessionManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemController implements HttpHandler {
    private static final Gson jsonParser = new GsonBuilder().disableHtmlEscaping().create();

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

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Handle preflight requests from browsers
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery()); //items?id=123

                exchange.getResponseHeaders().set("Content-Type", "application/json");

                String responseJson;

                // Check if requesting a specific item by ID
                String itemId = params.get("id");
                if (itemId != null && !itemId.isEmpty()) {
                    var item = ItemRepository.getItemById(itemId);
                    if (item != null) {
                        responseJson = jsonParser.toJson(item);
                    } else {
                        exchange.sendResponseHeaders(404, -1); // Not found
                        return;
                    }
                }  else {

                    String sortBy = params.get("sortBy");
                    String[] color = null;
                    String[] category = null;
                    String colorString = params.get("color");
                    String categoryString = params.get("category");
                    if (colorString != null) {
                        color = colorString.split(",");
                    }
                    if (categoryString != null) {
                        category = categoryString.split(",");
                    }

                    responseJson = jsonParser.toJson(
                            ItemRepository.getItemsFilteredSorted(sortBy, category, color)
                    );
            }

                byte[] responseBytes = responseJson.getBytes();
                exchange.sendResponseHeaders(200, responseBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }

            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(400, -1); // 400 Bad Request
            }
        } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery()); //items?id=123
            String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
            String token = SessionManager.extractToken(cookieHeader);

            if (SessionManager.isAdmin(token)){
                try {
                    InputStream is = exchange.getRequestBody();
                    String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    Item requestItem = jsonParser.fromJson(requestBody, Item.class);
                    if (requestItem.getImages() == null && requestItem.getThumbnailUrl() != null) {
                        requestItem.setImages(new String[]{requestItem.getThumbnailUrl()});
                    }
                    // Handle case where body is empty object or null
                    if (requestItem == null) {
                        requestItem = new Item();
                    }

                    if (params.get("id") != null) { //Modifying an item
                        requestItem.setId(params.get("id"));
                        ItemRepository.modifyItem(requestItem);
                        Item responseItem = ItemRepository.getItemById(requestItem.getId());
                        String responseJson = jsonParser.toJson(responseItem);
                        byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);

                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, responseBytes.length);

                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(responseBytes);
                        }
                    } else {
                        // Create new item
                        String newId = UUID.randomUUID().toString();
                        requestItem.setId(newId);

                        if (requestItem.getImages() == null) {
                            requestItem.setImages(new String[]{});
                        }
                        
                        // Ensure other fields are initialized if null, to avoid SQL errors if columns are NOT NULL
                        // For now relying on repository handling, but ID is critical.
                        
                        ItemRepository.addItem(requestItem);
                        
                        // Return the new ID
                        Item responseItem = ItemRepository.getItemById(newId);

                        String responseJson = jsonParser.toJson(responseItem);
                        byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);
                        
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, responseBytes.length);

                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(responseBytes);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, -1);
                }
            } else {
                exchange.sendResponseHeaders(401, -1);
            }
        } else if ("DELETE".equalsIgnoreCase(exchange.getRequestMethod())) {
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery()); //items?id=123
            String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
            String token = SessionManager.extractToken(cookieHeader);

            if (SessionManager.isAdmin(token)){
                if (params.get("id") != null) {
                    ItemRepository.removeItem(params.get("id"));
                    exchange.sendResponseHeaders(200, -1);
                }
            }
        }
    }
}