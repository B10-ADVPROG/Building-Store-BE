package id.ac.ui.cs.advprog.buildingstore.authentication.model;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class KasirTest {

    @Test
    void testKasirConstructor() {
        String email = "kasir@example.com";
        String fullname = "Kasir User";
        String password = "kasirpassword";

        Kasir kasir = new Kasir(email, fullname, password);

        assertEquals(email, kasir.getEmail());
        assertEquals(fullname, kasir.getFullname());
        assertEquals(password, kasir.getPassword());
        assertEquals("Kasir", kasir.getRole());
    }
}
