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

    @Test
    void createSupplier() {
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        SupplierDTO result = supplierService.createSupplier(supplierDTO);

        assertNotNull(result);
        assertEquals(supplierDTO.getName(), result.getName());
        verify(supplierRepository).save(any(Supplier.class));
    }

    @Test
    void getAllSuppliers() {
        when(supplierRepository.findAll()).thenReturn(Arrays.asList(supplier));

        List<SupplierDTO> result = supplierService.getAllSuppliers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(supplier.getName(), result.get(0).getName());
    }

    @Test
    void getSupplierById() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.of(supplier));

        SupplierDTO result = supplierService.getSupplierById(id);

        assertNotNull(result);
        assertEquals(supplier.getName(), result.getName());
    }

    @Test
    void getSupplierById_notFound() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> supplierService.getSupplierById(id));
    }

    @Test
    void updateSupplier() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        SupplierDTO updatedDTO = SupplierDTO.builder()
                .id(id)
                .name("Updated Name")
                .contactPerson("Jane Smith")
                .phone("987-654-3210")
                .email("updated@example.com")
                .address("456 New St")
                .active(true)
                .build();

        SupplierDTO result = supplierService.updateSupplier(id, updatedDTO);

        assertNotNull(result);
        verify(supplierRepository).save(any(Supplier.class));
    }

    @Test
    void deleteSupplier() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.of(supplier));

        supplierService.deleteSupplier(id);

        verify(supplierRepository).save(any(Supplier.class));
    }
}