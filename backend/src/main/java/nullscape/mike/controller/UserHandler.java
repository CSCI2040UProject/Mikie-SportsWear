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


    private void modifyOther() {

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
                String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
                request.token = SessionManager.extractToken(cookieHeader);

                User requestingUser = SessionManager.getUserFromToken(request.token);

                User newUser;
                if (requestingUser.getUsername().equals(request.username)) { //Modifying yourself

                    SessionManager.removeSession(request.token);
                    newUser = UserRepository.modifyUser(requestingUser, new User((request.username == null) ? requestingUser.getUsername() : request.username, (request.password == null) ? requestingUser.getPassword() : request.password, requestingUser.isAdmin()));
                    String newCookieHeader = SessionManager.CreateCookieHeader(SessionManager.createSession(newUser));
                    exchange.getResponseHeaders().add("Set-Cookie", newCookieHeader);
                } else if (requestingUser.isAdmin()) {
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