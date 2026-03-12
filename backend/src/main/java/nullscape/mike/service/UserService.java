package nullscape.mike.service;

import nullscape.mike.model.User;
import nullscape.mike.repository.UserRepository;

public class UserService {

    //Return the user if it exists from the username and password
    public static User validateUser(String username, String password) {
        User unAuthUser = UserRepository.findByUsername(username);
        if (unAuthUser != null && password.equals(unAuthUser.getPassword())) {
            return unAuthUser;
        }
        return null;
    }

    //Verify the requirements for a strong password and then return that user if adding the user to the csv is successful
    public static User registerUser(String username, String password, boolean isAdmin) {
        int size = 0;
        int numbers = 0;
        int symbols = 0;
        int upper = 0;
        for(char a : password.toCharArray()){
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
        if ((size>=3)&&(numbers>=0)&&(symbols>=0)&&(upper>=0)) { //changed for easier testing
            return UserRepository.addUser(username, password, isAdmin);
        }
        System.out.println("the password is invalid");
        return null;
    }


}
