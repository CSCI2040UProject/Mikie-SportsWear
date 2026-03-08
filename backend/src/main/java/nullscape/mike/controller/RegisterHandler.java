package nullscape.mike.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.model.User;
import nullscape.mike.service.SessionManager;
import nullscape.mike.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RegisterHandler implements HttpHandler {
    private static final Gson jsonParser = new Gson();

    private static class Request {
        String username;
        String password;
    }

    static class Response {
        String username;
        boolean isAdmin;

        Response(String username, boolean admin) {
            this.username = username;
            this.isAdmin = admin;
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
            Request request = jsonParser.fromJson(new String(is.readAllBytes()), Request.class);
            // As long as the names of the variables in the java class line up with the names in the JSON gson sorta just figures it out

            User authUser = UserService.registerUser(request.username, request.password, false);

            if (authUser != null) { // Registration successful

                // Create a new token
                String token = nullscape.mike.service.SessionManager.createSession(authUser);

                // Format the token into a cookie for the browser
                String cookieHeader = SessionManager.CreateCookieHeader(token);

                // Add the headers
                exchange.getResponseHeaders().add("Set-Cookie", cookieHeader);
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                // Send back username and isAdmin
                Response response = new Response(authUser.getUsername(), authUser.isAdmin());
                String responseJson = jsonParser.toJson(response);
                byte[] responseBytes = responseJson.getBytes();

                exchange.sendResponseHeaders(200, responseBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }
            } else {
                // User already exists
                exchange.sendResponseHeaders(409, -1); // 409 Conflict
            }

        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, -1); // 400 Bad Request
        }
    }
}