package nullscape.mike.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.model.Item;
import nullscape.mike.repository.ItemRepository;
import nullscape.mike.service.ParsingService;
import nullscape.mike.service.SessionManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public class ItemController implements HttpHandler {
    private static final Gson jsonParser = new GsonBuilder().disableHtmlEscaping().create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Handle preflight requests from browsers
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) { // Getting one or more items
            try {
                Map<String, String> params = ParsingService.parseQueryParams(exchange.getRequestURI().getQuery()); //items?id=123

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
                }  else { // Since the frontend hasn't asked for a specific item we know it's asking for the catalog view

                    // Make sure that if the color or category isn't requested null gets passed to the get items method
                    String[] color = null;
                    String[] category = null;


                    String name = params.get("search");
                    String sortBy = params.get("sortBy");
                    String colorString = params.get("color");
                    String categoryString = params.get("category");


                    if (colorString != null) {
                        color = colorString.split(",");
                    }
                    if (categoryString != null) {
                        category = categoryString.split(",");
                    }

                    responseJson = jsonParser.toJson(
                            ItemRepository.getItemsParams(name, sortBy, category, color)
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

        } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) { // Adding or modifying an item
            Map<String, String> params = ParsingService.parseQueryParams(exchange.getRequestURI().getQuery()); //items?id=123

            String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie"); // Get the cookie header
            String token = SessionManager.extractToken(cookieHeader); // Strip the header for the token

            if (SessionManager.isAdmin(token)){ // Check the in memory list of logged in admins for the token
                try {
                    InputStream is = exchange.getRequestBody();
                    String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    Item requestItem = jsonParser.fromJson(requestBody, Item.class); // Turn the JSON request into a Java object

                    // If the item doesn't have full res images add the thumbnail
                    if (requestItem.getImages() == null && requestItem.getThumbnailUrl() != null) {
                        requestItem.setImages(new String[]{requestItem.getThumbnailUrl()});
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

                    } else { // Create new item

                        // Create a new random UUID for the product id
                        String newId = UUID.randomUUID().toString();
                        requestItem.setId(newId);

                        if (requestItem.getImages() == null) {
                            requestItem.setImages(new String[]{});
                        }
                        
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

            } else { // The user requesting isn't an admin
                exchange.sendResponseHeaders(401, -1);
            }

        } else if ("DELETE".equalsIgnoreCase(exchange.getRequestMethod())) { // Delete an item
            Map<String, String> params = ParsingService.parseQueryParams(exchange.getRequestURI().getQuery()); //items?id=123
            String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
            String token = SessionManager.extractToken(cookieHeader);

            if (SessionManager.isAdmin(token)){
                if (params.get("id") != null) {
                    ItemRepository.removeItem(params.get("id"));
                    exchange.sendResponseHeaders(200, -1);
                } else {
                    exchange.sendResponseHeaders(400, -1);
                }
            } else {
                exchange.sendResponseHeaders(401, -1);
            }
        }
    }
}