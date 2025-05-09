package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.factory.SupplierFactory;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierFactory supplierFactory;

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

        when(supplierFactory.createSupplier(any(SupplierDTO.class))).thenReturn(supplier);
        when(supplierFactory.createSupplierDTO(any(Supplier.class))).thenReturn(supplierDTO);
    }

    @Test
    void shouldCreateSupplier() {
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        SupplierDTO result = supplierService.createSupplier(supplierDTO);

        assertNotNull(result);
        assertEquals(supplierDTO.getName(), result.getName());
        verify(supplierRepository).save(any(Supplier.class));
        verify(supplierFactory).createSupplier(any(SupplierDTO.class));
        verify(supplierFactory).createSupplierDTO(any(Supplier.class));
    }

    @Test
    void shouldGetAllSuppliers() {
        when(supplierRepository.findAll()).thenReturn(Collections.singletonList(supplier));

        List<SupplierDTO> result = supplierService.getAllSuppliers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(supplierFactory).createSupplierDTO(any(Supplier.class));
    }

    @Test
    void shouldGetSupplierById() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.of(supplier));

        SupplierDTO result = supplierService.getSupplierById(id);

        assertNotNull(result);
        assertEquals(supplierDTO.getName(), result.getName());
        verify(supplierFactory).createSupplierDTO(any(Supplier.class));
    }

    @Test
    void shouldThrowExceptionWhenSupplierNotFound() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> supplierService.getSupplierById(id));
    }

    @Test
    void shouldUpdateSupplier() {
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
        verify(supplierFactory).createSupplierDTO(any(Supplier.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdateSupplierNotFound() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        SupplierDTO updatedDTO = SupplierDTO.builder()
                .id(id)
                .name("Updated Name")
                .contactPerson("Jane Smith")
                .phone("987-654-3210")
                .email("updated@example.com")
                .address("456 New St")
                .active(true)
                .build();

        assertThrows(IllegalArgumentException.class, () -> supplierService.updateSupplier(id, updatedDTO));
    }

    @Test
    void shouldDeleteSupplier() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.of(supplier));

        supplierService.deleteSupplier(id);

        verify(supplierRepository).save(any(Supplier.class));
    }

    @Test
    void shouldThrowExceptionWhenDeleteSupplierNotFound() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> supplierService.deleteSupplier(id));
    }
}