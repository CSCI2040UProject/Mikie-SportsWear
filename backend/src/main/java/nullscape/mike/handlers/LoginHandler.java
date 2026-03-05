package nullscape.mike.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class LoginHandler implements HttpHandler {
    private static final Gson jsonParser = new Gson();

    private static class LoginRequest {
        String username;
        String password;
    }

    private static class LoginResponse {
        int token;
        String username;

        LoginResponse(int token , String username) {
            this.token = token;
            this.username = username;
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");

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
            LoginRequest loginReq = jsonParser.fromJson(new String(is.readAllBytes()), LoginRequest.class);
            String message = "";
            // As long as the names of the variables in the java class line up with the names in the JSON gson sorta just figures it out

            // Check if an account with this username already exists
            try (BufferedReader br = new BufferedReader(new FileReader("backend/src/resources/userData.csv"))) {
                br.readLine(); // Skip the labels
                while (true) {
                    String line = br.readLine();

                    if (line == null) {
                        message = "Invalid username/password!";
                        break;
                    } else {
                        String[] currInfo = line.split(",");
                        if (loginReq.username.equals(currInfo[0]) && loginReq.password.equals(currInfo[1])) {
                            message = "Logged in!";
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            LoginResponse loginResponse = new LoginResponse(9999, loginReq.username);
            String response = "{\"message\": \"" + message + "\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            byte[] responseBytes = response.getBytes();
            exchange.sendResponseHeaders(200, responseBytes.length); // Use 401 for unauthorized

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }

        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, -1); // 400 Bad Request
        }
    }
}