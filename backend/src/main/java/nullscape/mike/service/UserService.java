package nullscape.mike.service;

import nullscape.mike.model.User;
import nullscape.mike.repository.UserRepository;

public class UserService {
    public static User validateUser(String username, String password) {
        User unAuthUser = UserRepository.findByUsername(username);
        if (unAuthUser != null && password.equals(unAuthUser.getPassword())) {
            return unAuthUser;
        }
        return null;
    }

    public static User registerUser(String username, String password, boolean isAdmin) {
        //TODO: filter the user inputs
            return UserRepository.addUser(username, password, isAdmin);
    }


}
