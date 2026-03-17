package nullscape.mike;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import nullscape.mike.controller.*;
import nullscape.mike.database.CSVMigration;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {

        // Run CSV migration on first startup (only migrates if tables are empty)
        CSVMigration.runMigration();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/helloworld/", new HelloWorldHandler());
        server.createContext("/api/login/", new LoginHandler());
        server.createContext("/api/register", new RegisterHandler());
        server.createContext("/api/logout", new LogoutHandler());
        server.createContext("/api/user", new UserHandler());
        server.createContext("/api/catalog", new ItemController());
        server.createContext("/api/catalog/thumbnail", new ThumbnailController());

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