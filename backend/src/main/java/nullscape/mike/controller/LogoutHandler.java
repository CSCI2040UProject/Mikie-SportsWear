package nullscape.mike.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import nullscape.mike.service.SessionManager;

import java.io.IOException;
import java.io.OutputStream;

public class LogoutHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        // Only accept post requests
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try {
            String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
            String token = SessionManager.extractToken(cookieHeader);

            SessionManager.removeSession(token);

            String response = "Logged out successfully";

            //Set the cookie to something unfeasible so the browser knows to wipe its cookies
            exchange.getResponseHeaders().add(
                    "Set-Cookie",
                    "auth_token=; Path=/; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; SameSite=Lax"
            );
            exchange.getResponseHeaders().set("Content-Type", "text/plain");

            byte[] responseBytes = response.getBytes();
            exchange.sendResponseHeaders(200, responseBytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }

        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, -1);
        }
    }
}