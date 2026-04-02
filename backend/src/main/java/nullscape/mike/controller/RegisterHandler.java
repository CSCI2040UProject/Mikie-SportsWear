package nullscape.mike.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.model.User;
import nullscape.mike.repository.UserRepository;
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
            Request request = jsonParser.fromJson(new String(is.readAllBytes()), Request.class);
            // As long as the names of the variables in the java class line up with the names in the JSON gson sorta just figures it out

            // Check if the username/password is missing
            if (request.username == null || request.username.trim().isEmpty() || request.password == null || request.password.trim().isEmpty()) {
                Response response = new Response("Username and password are required");
                String responseJson = jsonParser.toJson(response);
                byte[] responseBytes = responseJson.getBytes();

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(400, responseBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }
                return;
            }

            // Check if the username already exists
            // Check the database first to see if the username is already taken
            User existUser = UserRepository.findByUsername(request.username);
            if (existUser != null) {
                Response response = new Response("Username already exists");
                String responseJson = jsonParser.toJson(response);
                byte[] responseBytes = responseJson.getBytes();

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(409, responseBytes.length); // Conflict code

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }
                return;
            }


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
                // if it is not an existing user but registration fails send a 400 error
                Response response = new Response("Registration failed");
                String responseJson = jsonParser.toJson(response);
                byte[] responseBytes = responseJson.getBytes();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(400, responseBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, -1); // 400 Bad Request
        }
    }
}