package id.ac.ui.cs.advprog.buildingstore.authentication.factory;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.Administrator;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.Kasir;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserFactoryTest {

    @Test
    public void testCreateKasir() {
        User kasir = UserFactory.createUser("kasir", "kasir@gmail.com", "Kasir Test", "kasirpass");

        assertInstanceOf(Kasir.class, kasir);
        assertEquals("Kasir", kasir.getRole());
        assertEquals("kasir@gmail.com", kasir.getEmail());
    }

    @Test
    public void testCreateAdministrator() {
        User admin = UserFactory.createUser("administrator", "admin@gmail.com", "Admin Test", "adminpass");

        assertInstanceOf(Administrator.class, admin);
        assertEquals("Administrator", admin.getRole());
        assertEquals("admin@gmail.com", admin.getEmail());
    }

    @Test
    public void testInvalidRoleThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            UserFactory.createUser("manager", "manager@gmail.com", "Manager Test", "managerpass");
        });

        assertEquals("Unknown role: manager", exception.getMessage());
    }
}
