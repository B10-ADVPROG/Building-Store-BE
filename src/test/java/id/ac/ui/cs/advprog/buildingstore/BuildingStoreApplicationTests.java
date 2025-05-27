package id.ac.ui.cs.advprog.buildingstore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
class BuildingStoreApplicationTests {

    @Test
    void contextLoads() {
    }
}
