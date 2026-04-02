package nullscape.mike.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.repository.ImageRepository;
import nullscape.mike.repository.ItemRepository;
import nullscape.mike.service.ParsingService;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;

public class ImageController implements HttpHandler {

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { // For the browser
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) { // The client is asking for an image
            Map<String, String> params = ParsingService.parsePathParams(exchange.getRequestURI().getPath(), "/api/images/{filename}");
            String filename = params.get("filename"); // UUID for the specific image
            String filePath = "uploads/" + filename;
            File file = new File(filePath);

            if (!file.exists()) { // No image with that UUID found
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            // Convert the image into raw bytes
            // As long as we tell the frontend that this data is a jpeg this works
            byte[] bytes = Files.readAllBytes(file.toPath());

            // Tell the frontend that the content is an image
            exchange.getResponseHeaders().add("Content-Type", "image/jpeg");

            exchange.sendResponseHeaders(200, bytes.length);

            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }

        else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) { // The client is uploading an image
            Map<String, String> params = ParsingService.parseQueryParams(exchange.getRequestURI().getQuery()); //items?id=123

            // Add the image UUID returned from ImageRepository to the item database
            ItemRepository.addImage(params.get("id"), ImageRepository.addImage(exchange.getRequestBody()));

            exchange.sendResponseHeaders(200, -1);
        }
    }
}