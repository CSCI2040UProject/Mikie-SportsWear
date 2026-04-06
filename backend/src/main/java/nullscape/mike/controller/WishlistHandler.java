package nullscape.mike.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.model.Item;
import nullscape.mike.model.User;
import nullscape.mike.repository.WishlistRepository;
import nullscape.mike.service.SessionManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class WishlistHandler implements HttpHandler {
    private static final Gson jsonParser = new Gson();

    private static class Request {
        String productId;
        String productName;
        String price;
        String image;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Handle preflight
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;

        }
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "http://localhost:5173");
        exchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");

        // Auth check
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
        String token = SessionManager.extractToken(cookieHeader);
        User user = SessionManager.getUserFromToken(token);

        if (user == null) {
            exchange.sendResponseHeaders(401, -1);
            return;
        }

        String method = exchange.getRequestMethod().toUpperCase();

        //fetch the user's wishlist
        if ("GET".equals(method)) {
            List<Item> items = WishlistRepository.getWishlist(user.getUsername());
            sendJson(exchange, 200, jsonParser.toJson(items));

            // add an item
        } else if ("POST".equals(method)) {
            InputStream is = exchange.getRequestBody();
            Request req = jsonParser.fromJson(new String(is.readAllBytes()), Request.class);

            if (req.productId == null || req.productName == null) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }

            Item item = new Item();
            item.setId(req.productId);
            item.setName(req.productName);
            item.setPrice(req.price);
            item.setImage(req.image);

            WishlistRepository.addItem(user.getUsername(), item);
            sendJson(exchange, 200, "{\"success\": true}");

            // DELETE an item
        } else if ("DELETE".equals(method)) {
            InputStream is = exchange.getRequestBody();
            Request req = jsonParser.fromJson(new String(is.readAllBytes()), Request.class);

            if (req.productId == null) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }

            WishlistRepository.removeItem(user.getUsername(), req.productId);
            sendJson(exchange, 200, "{\"success\": true}");

        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}