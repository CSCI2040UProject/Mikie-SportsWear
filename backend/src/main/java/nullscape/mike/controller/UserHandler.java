package nullscape.mike.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.model.User;
import nullscape.mike.repository.UserRepository;
import nullscape.mike.service.SessionManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class UserHandler implements HttpHandler {
    private static final Gson jsonParser = new Gson();

    //These classes are needed so that Gson can put the incoming JSON objects into java objects
    private static class Request {
        String username;
        String password;
        String token;
        boolean isAdmin;
    }

    private static class Response {
        String username;
        String password;
        String token;
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

        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                Request request = jsonParser.fromJson(new String(is.readAllBytes()), Request.class);

                if (request == null) {
                    exchange.sendResponseHeaders(400, -1);
                    return;
                }

                String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
                request.token = SessionManager.extractToken(cookieHeader);

                User requestingUser = SessionManager.getUserFromToken(request.token);

                if (requestingUser == null) { //If the requesting user isn't logged in
                    exchange.sendResponseHeaders(401, -1);
                    return;
                }

                User newUser;
                if (request.password != null) { //Modifying yourself
                    SessionManager.removeSession(request.token);

                    String updatedUsername = (request.username == null || request.username.trim().isEmpty()) ? requestingUser.getUsername() : request.username;
                    String updatedPassword = (request.password.trim().isEmpty()) ? requestingUser.getPassword() : request.password;

                    newUser = UserRepository.modifyUser(requestingUser, new User(updatedUsername, updatedPassword, requestingUser.isAdmin()));
                    String newCookieHeader = SessionManager.CreateCookieHeader(SessionManager.createSession(newUser));
                    exchange.getResponseHeaders().add("Set-Cookie", newCookieHeader);
                }
                else if (requestingUser.isAdmin()) { //Modifying someone else to be an admin
                    User userToModify = Objects.requireNonNull(UserRepository.findByUsername(request.username));
                    SessionManager.removeByUsername(request.username);
                    newUser = UserRepository.modifyUser(userToModify, new User(userToModify.getUsername(), userToModify.getPassword(), request.isAdmin));
                } else {
                    exchange.sendResponseHeaders(400, -1);
                    return;
                }


                exchange.getResponseHeaders().set("Content-Type", "application/json");

                // Send back username and isAdmin
                RegisterHandler.Response response = new RegisterHandler.Response(newUser.getUsername(), newUser.isAdmin());
                String responseJson = jsonParser.toJson(response);
                byte[] responseBytes = responseJson.getBytes();

                exchange.sendResponseHeaders(200, responseBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }

            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(400, -1); // 400 Bad Request
            }

        } else if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
        }
    }
}