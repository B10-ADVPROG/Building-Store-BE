package id.ac.ui.cs.advprog.buildingstore.authentication.factory;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.Administrator;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.Kasir;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class UserFactoryTest {

    @Test
    public void testKasirFactoryCreatesKasir() {
        UserFactory factory = new KasirFactory();
        User kasir = factory.createUser("kasir@gmail.com", "Kasir Test", "kasirpass");

        assertInstanceOf(Kasir.class, kasir);
        assertEquals("Kasir", kasir.getRole());
        assertEquals("kasir@gmail.com", kasir.getEmail());
        assertEquals("Kasir Test", kasir.getFullname());
    }

    @Test
    public void testAdministratorFactoryCreatesAdministrator() {
        UserFactory factory = new AdminFactory();
        User admin = factory.createUser("admin@gmail.com", "Admin Test", "adminpass");

        assertInstanceOf(Administrator.class, admin);
        assertEquals("Administrator", admin.getRole());
        assertEquals("admin@gmail.com", admin.getEmail());
        assertEquals("Admin Test", admin.getFullname());
    }


}