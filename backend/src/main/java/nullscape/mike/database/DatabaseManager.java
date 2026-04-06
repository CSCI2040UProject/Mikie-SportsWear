package nullscape.mike.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_PATH = System.getenv("DB_PATH") != null
        ? System.getenv("DB_PATH")
        : "./data/mikie_sportswear.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static Connection connection = null;

    private DatabaseManager() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                initializeDatabase();
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    private static void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // Create users table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    username TEXT PRIMARY KEY,
                    password TEXT NOT NULL,
                    is_admin INTEGER NOT NULL DEFAULT 0
                )
            """);

            // Create items table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS items (
                    product_id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    description TEXT,
                    categories TEXT,
                    price TEXT,
                    color TEXT,
                    other_colors TEXT,
                    product_url TEXT,
                    thumbnail_url TEXT,
                    image_urls TEXT
                )
            """);

            // Create wishlist table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS wishlist (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                product_id TEXT NOT NULL,
                product_name TEXT NOT NULL,
                price REAL NOT NULL,
                image TEXT,
                UNIQUE(username, product_id),
                FOREIGN KEY (username) REFERENCES users(username)
            )
        """);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
