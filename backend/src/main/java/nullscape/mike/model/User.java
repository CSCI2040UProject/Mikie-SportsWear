package nullscape.mike.model;

public class User {
    // Initialize variables
    private final String username;
    private final String password;
    private boolean isAdmin;
    // private ArrayList<String> wishlist;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return this.username;
    }

    // Probably not a good idea to keep this here
    public String getPassword() {
        return this.password;
    }

    // Setter methods for username and password likely aren't required for now
    public void setAdmin() {
        this.isAdmin = !this.isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
