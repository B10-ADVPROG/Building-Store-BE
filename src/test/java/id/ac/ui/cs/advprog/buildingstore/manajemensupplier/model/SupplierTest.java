package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {

    @Test
    void testPrePersistGeneratesUUID() {
        // Create a supplier without ID
        Supplier supplier = Supplier.builder()
                .name("Test Supplier")
                .contactPerson("John Doe")
                .phone("12345")
                .email("test@example.com")
                .address("Test Address")
                .build();
        
        // Manually call onCreate method
        supplier.onCreate();
        
        // Verify ID was generated
        assertNotNull(supplier.getId());
    }
    
    @Test
    void testPrePersistDoesNotOverrideExistingId() {
        // Create a supplier with predefined ID
        java.util.UUID predefinedId = java.util.UUID.randomUUID();
        Supplier supplier = Supplier.builder()
                .id(predefinedId)
                .name("Test Supplier")
                .contactPerson("John Doe")
                .phone("12345")
                .email("test@example.com")
                .address("Test Address")
                .build();
        
        // Manually call onCreate method
        supplier.onCreate();
        
        // Verify ID was not changed
        assertEquals(predefinedId, supplier.getId());
    }
}