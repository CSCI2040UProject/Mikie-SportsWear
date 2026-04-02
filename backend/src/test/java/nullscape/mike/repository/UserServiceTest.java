package nullscape.mike.repository;

import nullscape.mike.model.Item;
import nullscape.mike.model.User;
import nullscape.mike.service.UserService;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    public void testLogin_validCredentials() {

        String username = "validUser_" + System.currentTimeMillis();//This is for unique usernames
        String password = "correctPass";

        UserRepository.addUser(username, password, false);

        User result = UserService.validateUser(username, password);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    public void testLogin_invalidCredentials() {

        String username = "testUser_" + System.currentTimeMillis();

        UserRepository.addUser(username, "correctPass", false);

        User result = UserService.validateUser(username, "wrongPass");

        assertNull(result);
    }

    @Test
    public void testSignup_newUser() {

        String username = "newUser_" + System.currentTimeMillis();
        String password = "newPass123";

        User newUser = UserRepository.addUser(username, password, false);

        assertNotNull(newUser);
        assertEquals(username, newUser.getUsername());

        User loginUser = UserService.validateUser(username, password);
        assertNotNull(loginUser);
    }
}