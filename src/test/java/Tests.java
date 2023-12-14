import org.example.AppUser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Tests {

    @BeforeAll
    public static void setup() {
    }

    @AfterAll
    public static void teardown() {
    }

    @Test
    public void testAddUserToDB() {
        AppUser testUser = new AppUser(0, "test@example.com", LocalDate.of(1990, 1, 1), 30);

        testUser.addUserToDB();

        Optional<AppUser> retrievedUser = AppUser.findUserByEmail("test@example.com");

        assertTrue(retrievedUser.isPresent());
        assertEquals("test@example.com", retrievedUser.get().getEmail());

        testUser.deleteUserFromDB();
    }

    @Test
    public void testGetAllUsers() {
        List<AppUser> allUsers = AppUser.getAllUsers();

        assertNotNull(allUsers);
        assertTrue(allUsers.size() > 0);
    }

    @Test
    public void testFindUserByEmail() {
        AppUser testUser = new AppUser(0, "test@example.com", LocalDate.of(1990, 1, 1), 30);
        testUser.addUserToDB();

        Optional<AppUser> foundUser = AppUser.findUserByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());

        testUser.deleteUserFromDB();
    }

    @Test
    public void testDeleteUserFromDB() {
        AppUser testUser = new AppUser(0, "test@example.com", LocalDate.of(1990, 1, 1), 30);
        testUser.addUserToDB();

        testUser.deleteUserFromDB();

        Optional<AppUser> deletedUser = AppUser.findUserByEmail("test@example.com");

        assertFalse(deletedUser.isPresent());
    }

}
