package nullscape.mike.repository;

import nullscape.mike.model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

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
                        boolean isAdmin = currInfo[2].equals("true");
                        return new User(username, password, isAdmin);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Write a new user to the CSV
    public static User addUser(String username, String password, boolean isAdmin) {
        User existingUser = UserRepository.findByUsername(username);
        if (existingUser == null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/resources/userData.csv", true))) {
                String line = username + "," + password + "," + isAdmin  + "\n";
                bw.write(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new User(username, password, isAdmin);
        }
        return null;
    }

    //Remove a user from the CSV file matching the credentials from a User object
    public static void removeUser(User user) {
        List<String> data = new ArrayList<>(); //all the current data from the CSV file is saved here
        if (findByUsername(user.getUsername()) != null) {
            try (BufferedReader br = new BufferedReader(new FileReader("src/resources/userData.csv"))) {
                br.readLine(); // Skip the labels
                while (true) {
                    String line = br.readLine();

                    if (line == null) {
                        break;
                    } else {
                        String[] currInfo = line.split(",");
                        if (!currInfo[0].equals(user.getUsername())) { //Skip the user we're trying to remove
                            data.add(line);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Write the current data back to the file
        try (FileWriter fw = new FileWriter("src/resources/userData.csv")) {
            BufferedWriter out = new BufferedWriter(fw);
            out.write("Username,Password,isAdmin\n");
            for (String line : data) {
                out.write(line + "\n");
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This has to remove the user then add it back to the file
    public static User modifyUser(User existingUser, User newUser) {
        UserRepository.removeUser(existingUser);
        return (UserRepository.addUser(newUser.getUsername(), newUser.getPassword(), newUser.isAdmin()));
    }
}




