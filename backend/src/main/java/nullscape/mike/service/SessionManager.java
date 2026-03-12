package nullscape.mike.service;

import nullscape.mike.model.User;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    // Map the token to a User
    private static final Map<String, User> activeSessions = new HashMap<>();

    // Called when a user successfully logs in
    public static String createSession(User user) {
        // Generate a random token
        String token = UUID.randomUUID().toString();

        activeSessions.put(token, user);
        return token;
    }

    //Take a token and format it into a header that the browser accepts
    public static String CreateCookieHeader(String token) {
        String expires = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneOffset.UTC).plusDays(1));
        return "auth_token=" + token + "; Path=/; Max-Age=86400; Expires=" + expires + "; SameSite=Lax";
    }

    // Called on protected routes to check if the user's cookie is valid
    public static User getUserFromToken(String token) {
        if (token == null) return null;
        return activeSessions.get(token);
    }

    // Called when a user logs out
    public static void removeSession(String token) {
        if (token != null) {
            activeSessions.remove(token);
        }
    }

    //Extracts the token from the cookie
    public static String extractToken(String cookieHeader) {
        if (cookieHeader == null || cookieHeader.isEmpty()) {
            return null;
        }

        String[] cookies = cookieHeader.split(";");

        for (String cookie : cookies) {
            String trimmed = cookie.trim();
            if (trimmed.startsWith("auth_token=")) {
                return trimmed.substring("auth_token=".length());
            }
        }

        return null;
    }

    // Called to remove a user's session by username
    public static void removeByUsername(String username) {
        if (username != null) {
            activeSessions.values().removeIf(user -> username.equals(user.getUsername()));
        }
    }
}