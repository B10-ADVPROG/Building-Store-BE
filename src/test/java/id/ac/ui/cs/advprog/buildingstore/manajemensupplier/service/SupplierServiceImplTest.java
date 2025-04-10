package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private Supplier supplier;
    private SupplierDTO supplierDTO;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        supplier = Supplier.builder()
                .id(id)
                .name("ABC Building Supplies")
                .contactPerson("John Doe")
                .phone("123-456-7890")
                .email("abc@example.com")
                .address("123 Main St")
                .active(true)
                .build();

        supplierDTO = SupplierDTO.builder()
                .id(id)
                .name("ABC Building Supplies")
                .contactPerson("John Doe")
                .phone("123-456-7890")
                .email("abc@example.com")
                .address("123 Main St")
                .active(true)
                .build();
    }
}