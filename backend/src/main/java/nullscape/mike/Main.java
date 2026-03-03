package nullscape.mike;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.OutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        var helloContext = server.createContext("/api/helloworld/", new HelloWorldHandler());
        helloContext.getFilters().add(new CorsFilter()); //do this instead for security bypass

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

//this is just the CORS security stuff moved to a class so that you don't have to put it in every single path
//this is needed because browsers don't like talking to endpoints that don't match where it's getting the website from Eg localhost:8080 vs localhost:5173
class CorsFilter extends Filter {
    @Override
    public String description() {
        return "Adds CORS headers to responses";
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        chain.doFilter(exchange);
    }
}