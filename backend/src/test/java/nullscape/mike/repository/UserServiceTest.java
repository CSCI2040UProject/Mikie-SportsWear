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

        String username = "validUser_" + System.currentTimeMillis();//Super unique username
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

        // Use unique username
        String username = "newUser_" + System.currentTimeMillis();
        String password = "newPass123";

        // Act: create new user
        User newUser = UserRepository.addUser(username, password, false);

        // Assert: user is created
        assertNotNull(newUser);
        assertEquals(username, newUser.getUsername());

        // Verify user can log in
        User loginUser = UserService.validateUser(username, password);
        assertNotNull(loginUser);
    }
}