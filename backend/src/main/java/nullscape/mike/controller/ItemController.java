package nullscape.mike.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.model.Item;
import nullscape.mike.service.ItemService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

public class ItemController implements HttpHandler {
    private static final Gson jsonParser = new Gson();

    private static class Request {
        String itemName;
        String itemDescription;
        String itemPrice;
        String itemTags;
        String itemColour;
        String itemOtherColours;
        String[] itemImages;
    }

    static class Response {
        String itemName;
        String itemDescription;

        Response(String itemName, String itemDescription) {
            this.itemName = itemName;
            this.itemDescription = itemDescription;
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Handle preflight requests from browsers
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        // Only accept POST requests
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try {
            InputStream is = exchange.getRequestBody();
            ItemController.Request request = jsonParser.fromJson(new String(is.readAllBytes()), ItemController.Request.class);

            // Decode Base64 images to bytes
            byte[][] imageBytes = new byte[request.itemImages.length][];
            for (int i = 0; i < request.itemImages.length; i++) {
                imageBytes[i] = Base64.getDecoder().decode(request.itemImages[i]);
            }
            Item newItem = ItemService.makeItem(request.itemName, request.itemDescription, request.itemPrice, request.itemTags, request.itemColour, request.itemOtherColours, imageBytes);
            if (newItem != null) {
                // If item creation was successful
                // Send back item entry
                ItemController.Response response = new ItemController.Response(newItem.getName(), newItem.getDescription());
                String responseJson = jsonParser.toJson(response);
                byte[] responseBytes = responseJson.getBytes();

                exchange.sendResponseHeaders(200, responseBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, -1); // 400 Bad Request
        }
    }
}