package nullscape.mike;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/helloworld/", new HelloWorldHandler());
        server.createContext("/api/login/", new LoginHandler());

        server.start();
        System.out.println("Server is listening on port 8080...");
    }
}       

class HelloWorldHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "Hello from the java backend server!";

        exchange.getResponseHeaders().set("Content-Type", "text/plain"); //tell the browser that the content type is plain text as opposed to JSON
        exchange.sendResponseHeaders(200, response.length()); //set the headers for the response
        //HTTP code 200 = OK

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}

// This is mostly just an example for POST requests and should probably be moved to another file
// It also always assumes a successful auth
class LoginHandler implements HttpHandler {
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
            // As long as the names of the variables in the java class line up with the names in the JSON gson sorta just figures it out

            System.out.println("Username: " + loginReq.username + " Password: " + loginReq.password);

            LoginResponse loginResponse = new LoginResponse(9999, loginReq.username);
            String response;
            response = jsonParser.toJson(loginResponse);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            byte[] responseBytes = response.getBytes();
            exchange.sendResponseHeaders(200, responseBytes.length); // Use 401 for unauthorized

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }

        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, -1); // 400 Bad Request
        }
    }
}