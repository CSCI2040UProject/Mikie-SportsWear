package nullscape.mike.repository;

import nullscape.mike.database.DatabaseManager;
import nullscape.mike.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishlistRepository {

    public static List<Item> getWishlist(String username) {
        String sql = "SELECT product_id, product_name, price, image FROM wishlist WHERE username = ?";
        List<Item> items = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getString("product_id"));
                item.setName(rs.getString("product_name"));
                item.setPrice(rs.getString("price"));
                item.setImage(rs.getString("image"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static boolean addItem(String username, Item item) {
        String sql = "INSERT OR IGNORE INTO wishlist (username, product_id, product_name, price, image) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, item.getId());
            pstmt.setString(3, item.getName());
            pstmt.setString(4, item.getPrice());
            pstmt.setString(5, item.getImage());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean removeItem(String username, String productId) {
        String sql = "DELETE FROM wishlist WHERE username = ? AND product_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, productId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}