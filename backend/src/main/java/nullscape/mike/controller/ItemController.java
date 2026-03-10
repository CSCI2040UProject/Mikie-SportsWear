package nullscape.mike.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.model.Catalog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ItemController implements HttpHandler {
    private static final Gson jsonParser = new GsonBuilder().disableHtmlEscaping().create();

    private Map<String, String> parseQueryParams(String query) { // this should get the parameters passed in through the url
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

        try {
            InputStream is = exchange.getRequestBody();
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery()); //params.get("id"); e.g. /items?id=123
            //TODO Check if the frontend is requesting a specific item or all if the id is blank
            //We could also check if the frontend is requesting a specific slice of the data instead of all of it

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