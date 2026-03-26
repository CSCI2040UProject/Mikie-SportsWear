package nullscape.mike.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

public class ImageController implements HttpHandler {

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        System.out.println("Upload endpoint hit!");

        Files.createDirectories(Paths.get(UPLOAD_DIR));

        String fileName = UUID.randomUUID().toString() + ".jpg";
        Path filePath = Paths.get(UPLOAD_DIR + fileName);

        InputStream input = exchange.getRequestBody();
        Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);

        String response = "/uploads/" + fileName;

        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}