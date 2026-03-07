package nullscape.mike.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.model.Catalog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ItemController implements HttpHandler {
    private static final Gson jsonParser = new GsonBuilder().disableHtmlEscaping().create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Handle preflight requests from browsers
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        // Only accept POST requests
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try {
            InputStream is = exchange.getRequestBody();

            // As long as the names of the variables in the java class line up with the names in the JSON gson sorta just figures it out

            exchange.getResponseHeaders().set("Content-Type", "application/json");

            String responseJson = jsonParser.toJson(Catalog.catalog);
            byte[] responseBytes = responseJson.getBytes();

            exchange.sendResponseHeaders(200, responseBytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }


        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, -1); // 400 Bad Request
        }
    }
}