package nullscape.mike.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ItemController implements HttpHandler {
    private static final Gson jsonParser = new Gson();

    private static class Request {
        String itemName;
        String itemDescription;
        String itemPrice;
        String itemTags;
        File[] itemImages;
    }

    static class Response {
        String username;
        boolean isAdmin;
        String errorMessage;

        Response(String username, boolean admin) {
            this.username = username;
            this.isAdmin = admin;
        }

        Response(String errorMessage){
            this.errorMessage = errorMessage;
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
        }
        catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, -1); // 400 Bad Request
        }
    }
}