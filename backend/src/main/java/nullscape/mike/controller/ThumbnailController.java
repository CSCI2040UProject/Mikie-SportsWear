package nullscape.mike.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.repository.ItemRepository;
import nullscape.mike.service.ParsingService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class ThumbnailController implements HttpHandler {

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
            Map<String, String> params = ParsingService.parseQueryParams(exchange.getRequestURI().getQuery()); //items?id=123

            exchange.getResponseHeaders().set("Content-Type", "application/json");

            String response = "";

            // This endpoint requires an id
            String itemId = params.get("id");
            if (itemId != null && !itemId.isEmpty()) {
                var item = ItemRepository.getThumbnail(itemId);
                if (item != null) {
                    response = item;
                } else {
                    exchange.sendResponseHeaders(400, -1); // 400 Bad Request
                    return;
                }
            } else {
                exchange.sendResponseHeaders(400, -1); // 400 Bad Request
            }

            exchange.sendResponseHeaders(200, response.length());

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }

        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, -1); // 400 Bad Request
        }
    }
}
