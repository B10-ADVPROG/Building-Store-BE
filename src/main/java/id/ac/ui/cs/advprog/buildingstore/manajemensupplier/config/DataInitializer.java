package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.config;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.repository.SupplierRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Profile("local")
public class DataInitializer {
    
    @Autowired
    private SupplierRepository supplierRepository;
    
    @PostConstruct
    @Transactional
    public void init() {
        System.out.println("Initializing test data for suppliers...");
        
        try {
            if (supplierRepository.count() == 0) {
                UUID testId1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
                UUID testId2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
                
                if (!supplierRepository.existsById(testId1)) {
                    Supplier supplier1 = Supplier.builder()
                            .id(testId1)
                            .name("ABC Building Supplies")
                            .contactPerson("John Doe")
                            .phone("123-456-7890")
                            .email("abc@example.com")
                            .address("123 Main St")
                            .active(true)
                            .build();
                    supplierRepository.save(supplier1);
                }
                
                if (!supplierRepository.existsById(testId2)) {
                    Supplier supplier2 = Supplier.builder()
                            .id(testId2)
                            .name("XYZ Construction Materials")
                            .contactPerson("Jane Smith")
                            .phone("098-765-4321")
                            .email("xyz@example.com")
                            .address("456 Oak Ave")
                            .active(true)
                            .build();
                    supplierRepository.save(supplier2);
                }
                
                System.out.println("=== Test suppliers created successfully ===");
                System.out.println("Total suppliers in database: " + supplierRepository.count());
            }
        } catch (Exception e) {
            System.err.println("Error initializing test data: " + e.getMessage());
        }
    }
}