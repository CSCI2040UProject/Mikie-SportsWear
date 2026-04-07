package nullscape.mike.repository;

import com.google.gson.Gson;
import nullscape.mike.database.DatabaseManager;
import nullscape.mike.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemRepository {

    private static final Gson gson = new Gson();

    public static List<Item> getAllItems() { // I might delete this because it should almost never be used...
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
            String[] images = item.getImages() != null ? item.getImages() : new String[]{};
            pstmt.setString(10, serializeArray(images));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addImage(String productId, String imageUrl) {
        Item item = getItemById(productId);
        if (item == null) return;

        // Append new URL to existing images array
        String[] existingImages = item.getImages() != null ? item.getImages() : new String[]{};
        String[] updatedImages = new String[existingImages.length + 1];
        System.arraycopy(existingImages, 0, updatedImages, 0, existingImages.length);
        updatedImages[existingImages.length] = imageUrl;

        // Set thumbnail if one doesn't exist yet
        String thumbnail = item.getThumbnailUrl() != null ? item.getThumbnailUrl() : imageUrl;

        String sql = "UPDATE items SET image_urls = ?, thumbnail_url = ? WHERE product_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, serializeArray(updatedImages));
            pstmt.setString(2, thumbnail);
            pstmt.setString(3, productId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeItem(String productId) {
        String sql = "DELETE FROM items WHERE product_id = ?";

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
            params.add(serializeArray(
                    item.getImages().length == 0 ? new String[]{} : item.getImages()
            ));
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
    public static List<Item> getItemsParams(String search, String sortBy, String[] category, String[] color) {
        List<Item> items = new ArrayList<>();

        // Handle null sortBy safely
        String sortCondition = (sortBy != null) ? sortBy.split("-")[0] : "name";
        String direction     = (sortBy != null) ? sortBy.split("-")[1] : "asc";

        String orderColumn = switch (sortCondition.toLowerCase()) {
            case "price" -> "CAST(REPLACE(REPLACE(price, '$', ''), ',', '') AS REAL)";
            default      -> "name";
        };
        String orderDir = "desc".equalsIgnoreCase(direction) ? "DESC" : "ASC";

        // Build WHERE clause
        StringBuilder whereClause = new StringBuilder();
        List<String> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            whereClause.append("name LIKE ?");
            params.add("%" + search.trim() + "%");
        }

        if (category != null && category.length > 0) {
            if (!whereClause.isEmpty()) whereClause.append(" AND ");
            whereClause.append(buildLikeConditions("categories", category, params));
        }

        if (color != null && color.length > 0) {
            if (!whereClause.isEmpty()) whereClause.append(" AND ");
            whereClause.append(buildLikeConditions("color", color, params));
        }

        String sql = "SELECT product_id, name, price, thumbnail_url FROM items"
                + (whereClause.isEmpty() ? "" : " WHERE " + whereClause)
                + " ORDER BY " + orderColumn + " " + orderDir;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Inject params into the prepared statement
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getString("product_id"));
                    item.setName(rs.getString("name"));
                    item.setPrice(rs.getString("price"));
                    item.setThumbnailUrl(rs.getString("thumbnail_url"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static List<Item> getSimilarItems(String productId) {
        Item item = getItemById(productId);
        List<Item> items = new ArrayList<>();
        List<String> params = new ArrayList<>();

        String likeConditions = buildLikeConditions("categories", item.getCategories(), params);
        String sql = "SELECT product_id, name, price, thumbnail_url FROM items"
                + " WHERE " + likeConditions
                + " AND product_id != ?";
        params.add(productId); // exclude the current item

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Item similarItem = new Item();
                    similarItem.setId(rs.getString("product_id"));
                    similarItem.setName(rs.getString("name"));
                    similarItem.setPrice(rs.getString("price"));
                    similarItem.setThumbnailUrl(rs.getString("thumbnail_url"));
                    items.add(similarItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Builds "col LIKE ? AND col LIKE ?" and populates params with wrapped values
    private static String buildLikeConditions(String column, String[] values, List<String> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) sb.append(" AND ");
            sb.append(column).append(" LIKE ?");

            // categories values need JSON style wrapping like '%"men"%' because '%men%' will also include woMEN
            // color values use plain wrapping: '%black%'
            String wrapped = column.equals("categories")
                    ? "%\"" + values[i] + "\"%"
                    :  "%"  + values[i] + "%";
            params.add(wrapped); //this is pass by reference so that all the params are added later
        }
        return sb.toString();
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
