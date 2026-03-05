package nullscape.mike.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RegisterHandler implements HttpHandler {
    private static final Gson jsonParser = new Gson();

    private static class RegisterRequest {
        String username;
        String password;
    }

    private static class RegisterResponse {
        int token;
        String username;

        RegisterResponse(int token , String username) {
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
            RegisterRequest registerReq = jsonParser.fromJson(new String(is.readAllBytes()), RegisterRequest.class);
            String message = "";
            // As long as the names of the variables in the java class line up with the names in the JSON gson sorta just figures it out

            // Check if an account with this username already exists
            try (BufferedReader br = new BufferedReader(new FileReader("backend/src/resources/userData.csv"))) {
                br.readLine(); // Skip the labels
                while (true) {
                    String line = br.readLine();

                    if (line == null) {
                        break;
                    } else {
                        String existingUsername = line.split(",")[0];
                        if (existingUsername.equals(registerReq.username)) {
                            message = "Account already exists with this username!";
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Variable "response" should still be null if the username doesn't already exist
            if (message.isEmpty()) {
                // Write to the user database
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("backend/src/resources/userData.csv", true))) {
                    String line = registerReq.username + "," + registerReq.password + ",0\n";
                    bw.write(line);
                    message = "Success!!";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            RegisterResponse registerResponse = new RegisterResponse(9999, registerReq.username);

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