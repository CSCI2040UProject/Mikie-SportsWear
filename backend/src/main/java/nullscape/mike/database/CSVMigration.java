package nullscape.mike.database;

import nullscape.mike.model.Item;
import nullscape.mike.repository.ItemRepository;
import nullscape.mike.repository.UserRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CSVMigration {

    private static final String CSV_BASE_PATH = System.getProperty("user.dir") + "/src/resources/";

    public static void migrateUsers() {
        String checkSql = "SELECT COUNT(*) as count FROM users";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             ResultSet rs = checkStmt.executeQuery()) {

            if (rs.next() && rs.getInt("count") > 0) {
                System.out.println("Users table already contains data. Skipping migration.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_BASE_PATH + "userData.csv"))) {
            br.readLine(); // Skip header
            String line;
            int count = 0;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    boolean isAdmin = parts[2].trim().equals("true");

                    UserRepository.addUser(username, password, isAdmin);
                    count++;
                }
            }
            System.out.println("Migrated " + count + " users from CSV to database.");
        } catch (Exception e) {
            System.err.println("Error migrating users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void migrateItems() {
        String checkSql = "SELECT COUNT(*) as count FROM items";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             ResultSet rs = checkStmt.executeQuery()) {

            if (rs.next() && rs.getInt("count") > 0) {
                System.out.println("Items table already contains data. Skipping migration.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_BASE_PATH + "Nike_Dataset.csv"))) {
            br.readLine(); // Skip header
            String line;
            int count = 0;

            while ((line = br.readLine()) != null) {
                String[] currInfo = parseCSVLine(line);
                if (currInfo.length >= 9) {
                    Item item = new Item();
                    item.setId(currInfo[0]);
                    item.setName(currInfo[1]);
                    item.setDescription(currInfo[2]);
                    item.setCategories(cleanArrayString(currInfo[3]));
                    item.setPrice(currInfo[4]);
                    item.setColor(currInfo[5]);
                    item.setOtherColors(cleanArrayString(currInfo[6]));
                    item.setProductUrl(currInfo[7]);
                    item.setThumbnailUrl(currInfo[8]);
                    item.setImages(cleanArrayString(currInfo[9]));

                    ItemRepository.addItem(item);
                    count++;
                }
            }
            System.out.println("Migrated " + count + " items from CSV to database.");
        } catch (Exception e) {
            System.err.println("Error migrating items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString().trim());
        while(result.size() < 10) {
            result.add("");
        }
        return result.toArray(new String[0]);
    }

    private static String[] cleanArrayString(String arrString) {
        if (arrString == null || arrString.trim().isEmpty()) return new String[0];
        String cleaned = arrString.trim();
        if (cleaned.startsWith("[")) cleaned = cleaned.substring(1);
        if (cleaned.endsWith("]")) cleaned = cleaned.substring(0, cleaned.length() - 1);
        cleaned = cleaned.trim();
        if (cleaned.isEmpty()) return new String[0];

        if (cleaned.startsWith("\"")) cleaned = cleaned.substring(1);
        if (cleaned.endsWith("\"")) cleaned = cleaned.substring(0, cleaned.length() - 1);
        if (cleaned.startsWith("'")) cleaned = cleaned.substring(1);
        if (cleaned.endsWith("'")) cleaned = cleaned.substring(0, cleaned.length() - 1);

        return cleaned.split("[\"']\\s*,\\s*[\"']");
    }

    public static void runMigration() {
        System.out.println("Starting CSV migration...");
        migrateUsers();
        migrateItems();
        System.out.println("Migration completed.");
    }
}
