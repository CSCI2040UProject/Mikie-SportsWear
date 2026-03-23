package nullscape.mike.repository;

import com.google.gson.Gson;
import nullscape.mike.database.DatabaseManager;
import nullscape.mike.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemRepository {

    private static final Gson gson = new Gson();

    public static List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT product_id, name, description, categories, price, color, other_colors, product_url, thumbnail_url, image_urls FROM items";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static List<Item> getItemsGist() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT product_id, name, price, thumbnail_url FROM items";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getString("product_id"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getString("price"));
                item.setThumbnailUrl(rs.getString("thumbnail_url"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static Item getItemById(String productId) {
        String sql = "SELECT product_id, name, description, categories, price, color, other_colors, product_url, thumbnail_url, image_urls FROM items WHERE product_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToItem(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getThumbnail(String productId) {
        String sql = "SELECT thumbnail_url FROM items WHERE product_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("thumbnail_url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addItem(Item item) {
        String sql = "INSERT INTO items (product_id, name, description, categories, price, color, other_colors, product_url, thumbnail_url, image_urls) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getId());
            pstmt.setString(2, item.getName());
            pstmt.setString(3, item.getDescription());
            pstmt.setString(4, (serializeArray(item.getCategories())));
            pstmt.setString(5, item.getPrice());
            pstmt.setString(6, item.getColor());
            pstmt.setString(7, serializeArray(item.getOtherColors()));
            pstmt.setString(8, item.getProductUrl());
            pstmt.setString(9, item.getThumbnailUrl());
            pstmt.setString(10, serializeArray(item.getImages()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeItem(String productId) {
        String sql = "DELETE FROM items WHERE product_id IS ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void modifyItem(Item item) {
        List<String> updates = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (item.getName() != null) {
            updates.add("name = ?");
            params.add(item.getName());
        }
        if (item.getDescription() != null) {
            updates.add("description = ?");
            params.add(item.getDescription());
        }
        if (item.getCategories() != null) {
            updates.add("categories = ?");
            params.add(serializeArray(item.getCategories()));
        }
        if (item.getPrice() != null) {
            updates.add("price = ?");
            params.add(item.getPrice());
        }
        if (item.getColor() != null) {
            updates.add("color = ?");
            params.add(item.getColor());
        }
        if (item.getOtherColors() != null) {
            updates.add("other_colors = ?");
            params.add(serializeArray(item.getOtherColors()));
        }
        if (item.getProductUrl() != null) {
            updates.add("product_url = ?");
            params.add(item.getProductUrl());
        }
        if (item.getThumbnailUrl() != null) {
            updates.add("thumbnail_url = ?");
            params.add(item.getThumbnailUrl());
        }
        if (item.getImages() != null) {
            updates.add("image_urls = ?");
            params.add(serializeArray(item.getImages()));
        }

        if (updates.isEmpty()) {
            return; // Nothing to update
        }

        String sql = "UPDATE items SET " + String.join(", ", updates) + " WHERE product_id = ?";
        params.add(item.getId());

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Item mapResultSetToItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getString("product_id"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setCategories(deserializeArray(rs.getString("categories")));
        item.setPrice(rs.getString("price"));
        item.setColor(rs.getString("color"));
        item.setOtherColors(deserializeArray(rs.getString("other_colors")));
        item.setProductUrl(rs.getString("product_url"));
        item.setThumbnailUrl(rs.getString("thumbnail_url"));
        item.setImages(deserializeArray(rs.getString("image_urls")));
        return item;
    }

    private static String serializeArray(String[] array) {
        return (array == null ? null : gson.toJson(array));
    }

    private static String[] deserializeArray(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        return gson.fromJson(json, String[].class);
    }
}
