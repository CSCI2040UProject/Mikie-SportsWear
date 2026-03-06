package nullscape.mike.repository;

import nullscape.mike.model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class UserRepository {

    public static User findByUsername(String username) {
        // Check if an account with this username already exists
        try (BufferedReader br = new BufferedReader(new FileReader("src/resources/userData.csv"))) {
            br.readLine(); // Skip the labels
            while (true) {
                String line = br.readLine();

                if (line == null) {
                    break;
                } else {
                    String[] currInfo = line.split(",");
                    if (username.equals(currInfo[0])) {
                        String password = currInfo[1];
                        boolean isAdmin = currInfo[2].equals("1");
                        return new User(username, password, isAdmin);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User addUser(String username, String password, boolean isAdmin) {
        User existingUser = UserRepository.findByUsername(username);
        if (existingUser == null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/resources/userData.csv", true))) {
                String line = username + "," + password + "," + isAdmin + "\n";
                bw.write(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new User(username, password, isAdmin);
        }
        return null;
    }
}




