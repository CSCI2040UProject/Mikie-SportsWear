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
        int size = 0;
        int numbers = 0;
        int symbols = 0;
        int upper = 0;
        for(char a : username.toCharArray()){
            size++;
            if(Character.isDigit(a)){
                numbers++;
            }
            if(!Character.isLetterOrDigit(a) || !Character.isWhitespace(a)){
                symbols++;
            }
            if(Character.isUpperCase(a)){
                upper++;
            }
        }
        if ((size>=8)&&(numbers>=4)&&(symbols>=2)&&(upper>0)) {
            return UserRepository.addUser(username, password, isAdmin);
        }
        return null;
    }


}
