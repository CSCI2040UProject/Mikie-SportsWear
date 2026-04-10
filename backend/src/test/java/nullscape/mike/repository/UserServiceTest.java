package nullscape.mike.repository;

import nullscape.mike.model.User;
import nullscape.mike.service.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    public void testLogin_validCredentials() {
        String username = "validUser_" + System.currentTimeMillis();
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

    @Test
    void testLogin_nonExistentUser() {
        User result = UserService.validateUser("ghost_user_" + System.currentTimeMillis(), "anyPass");
        assertNull(result);
    }

    @Test
    void testLogin_emptyUsername() {
        User result = UserService.validateUser("", "password");
        assertNotNull(result);
    }

    @Test
    void testLogin_emptyPassword() {
        String username = "user_" + System.currentTimeMillis();
        UserRepository.addUser(username, "password", false);

        User result = UserService.validateUser(username, "");
        assertNull(result);
    }

    @Test
    void testSignup_duplicateUser() {
        String username = "dupUser_" + System.currentTimeMillis();
        String password = "pass";

        UserRepository.addUser(username, password, false);
        User second = UserRepository.addUser(username, password, false);

        assertNull(second);
    }

    @Test
    void testSignup_emptyUsername() {
        User user = UserRepository.addUser("", "password", false);
        assertNull(user);
    }

    @Test
    void testSignup_emptyPassword() {
        String username = "emptyPass_" + System.currentTimeMillis();

        User user = UserRepository.addUser(username, "", false);

        assertNotNull(user);
    }

    @Test
    void testLogin_caseSensitivity() {
        String username = "CaseUser_" + System.currentTimeMillis();
        String password = "Password123";

        UserRepository.addUser(username, password, false);

        User result = UserService.validateUser(username.toLowerCase(), password);

        assertNull(result);
    }

    @Test
    void testMultipleUsersLoginIsolation() {
        String user1 = "user1_" + System.currentTimeMillis();
        String user2 = "user2_" + System.currentTimeMillis();

        UserRepository.addUser(user1, "pass1", false);
        UserRepository.addUser(user2, "pass2", false);

        User result1 = UserService.validateUser(user1, "pass1");
        User result2 = UserService.validateUser(user2, "pass2");

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotEquals(result1.getUsername(), result2.getUsername());
    }
}