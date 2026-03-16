package nullscape.mike.repository;

import nullscape.mike.database.DatabaseManager;
import nullscape.mike.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public static User findByUsername(String username) {
        String sql = "SELECT username, password, is_admin FROM users WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String password = rs.getString("password");
                boolean isAdmin = rs.getInt("is_admin") == 1;
                return new User(username, password, isAdmin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User addUser(String username, String password, boolean isAdmin) {
        User existingUser = UserRepository.findByUsername(username);
        if (existingUser != null) {
            return null;
        }

        String sql = "INSERT INTO users (username, password, is_admin) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, isAdmin ? 1 : 0);
            pstmt.executeUpdate();

            return new User(username, password, isAdmin);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeUser(User user) {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User modifyUser(User existingUser, User newUser) {
        String sql = "UPDATE users SET username = ?, password = ?, is_admin = ? WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newUser.getUsername());
            pstmt.setString(2, newUser.getPassword());
            pstmt.setInt(3, newUser.isAdmin() ? 1 : 0);
            pstmt.setString(4, existingUser.getUsername());
            pstmt.executeUpdate();

            return new User(newUser.getUsername(), newUser.getPassword(), newUser.isAdmin());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}




