package id.ac.ui.cs.advprog.buildingstore.authentication.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserConstructorWithValidInputs() {
        User user = new User("user@example.com", "John Doe", "password123", "kasir");

        assertEquals("user@example.com", user.getEmail());
        assertEquals("John Doe", user.getFullname());
        assertEquals("password123", user.getPassword());
        assertEquals("Kasir", user.getRole()); // because getRole() returns display name
    }

    @Test
    void testSetEmailWithValidInput() {
        User user = new User();
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testSetEmailWithEmptyStringThrowsException() {
        User user = new User();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setEmail(""));
        assertEquals("Email cannot be empty", exception.getMessage());
    }

    @Test
    void testSetFullnameWithValidInput() {
        User user = new User();
        user.setFullname("Alice Smith");
        assertEquals("Alice Smith", user.getFullname());
    }

    @Test
    void testSetFullnameWithEmptyStringThrowsException() {
        User user = new User();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setFullname(""));
        assertEquals("Fullname cannot be empty", exception.getMessage());
    }

    @Test
    void testSetPasswordAndGetPassword() {
        User user = new User();
        user.setPassword("securePass!");
        assertEquals("securePass!", user.getPassword());
    }

    @Test
    void testSetRoleAsAdminAndKasir() {
        User user = new User();
        user.setRole("admin");
        assertEquals("Administrator", user.getRole());

        user.setRole("kasir");
        assertEquals("Kasir", user.getRole());
    }

    @Test
    void testSetRoleWithInvalidRoleThrowsException() {
        User user = new User();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setRole("invalidRole"));
        assertEquals("Unknown role: invalidRole", exception.getMessage());
    }
}
