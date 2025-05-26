package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.factory;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SupplierFactoryTest {
    
    private SupplierFactory factory;
    private UUID testId;
    
    @BeforeEach
    void setUp() {
        factory = new SupplierFactory();
        testId = UUID.randomUUID();
    }
    
    @Test
    void testCreateSupplier() {
        // Create a DTO
        SupplierDTO dto = SupplierDTO.builder()
                .id(testId)
                .name("ABC Supplies")
                .contactPerson("John Doe")
                .phone("123-456-7890")
                .email("abc@example.com")
                .address("123 Main St")
                .active(false) // Set to false to verify it's always set to true in the factory
                .build();
        
        // Convert to model
        Supplier supplier = factory.createSupplier(dto);
        
        // Verify all fields
        assertEquals(dto.getId(), supplier.getId());
        assertEquals(dto.getName(), supplier.getName());
        assertEquals(dto.getContactPerson(), supplier.getContactPerson());
        assertEquals(dto.getPhone(), supplier.getPhone());
        assertEquals(dto.getEmail(), supplier.getEmail());
        assertEquals(dto.getAddress(), supplier.getAddress());
        assertTrue(supplier.isActive()); // Should always be true for new suppliers
    }
    
    @Test
    void testCreateSupplierDTO() {
        // Create a model
        Supplier supplier = Supplier.builder()
                .id(testId)
                .name("XYZ Corporation")
                .contactPerson("Jane Smith")
                .phone("987-654-3210")
                .email("xyz@example.com")
                .address("456 Oak Ave")
                .active(true)
                .build();
        
        // Convert to DTO
        SupplierDTO dto = factory.createSupplierDTO(supplier);
        
        // Verify all fields
        assertEquals(supplier.getId(), dto.getId());
        assertEquals(supplier.getName(), dto.getName());
        assertEquals(supplier.getContactPerson(), dto.getContactPerson());
        assertEquals(supplier.getPhone(), dto.getPhone());
        assertEquals(supplier.getEmail(), dto.getEmail());
        assertEquals(supplier.getAddress(), dto.getAddress());
        assertEquals(supplier.isActive(), dto.isActive());
    }
}