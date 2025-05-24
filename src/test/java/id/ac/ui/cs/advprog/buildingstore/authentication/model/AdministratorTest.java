package id.ac.ui.cs.advprog.buildingstore.authentication.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdministratorTest {

    @Test
    void testAdministratorConstructor() {
        String email = "admin@example.com";
        String fullname = "Admin User";
        String password = "adminpass";

        Administrator admin = new Administrator(email, fullname, password);

        assertEquals(email, admin.getEmail());
        assertEquals(fullname, admin.getFullname());
        assertEquals(password, admin.getPassword());
        assertEquals("Administrator", admin.getRole());
    }
}
