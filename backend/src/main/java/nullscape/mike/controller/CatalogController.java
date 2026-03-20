package nullscape.mike.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.model.Catalog;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CatalogController implements HttpHandler {
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

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Handle preflight requests from browsers
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        // Only accept GET requests
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        // We could be able to tell if the user is trying to modify or add an item if they send a POST request

        try {
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery()); //items?id=123
            //TODO: Check if the frontend is requesting a specific item or all if the id is blank
            // If the frontend is requesting a specific item send all data
            //TODO: Check if the frontend is requesting a specific slice of the data instead of all of it
            //TODO: Only send the necessary data to the frontend when it's displaying the catalog overview
            // Like name, price, and id

            // Currently the frontend is getting the whole catalog just for one item
            // When this is changed the frontend will need to also change


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