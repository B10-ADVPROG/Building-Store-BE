package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.controller;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class SupplierControllerTest {

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private SupplierController supplierController;

    private SupplierDTO supplierDTO;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        supplierDTO = SupplierDTO.builder()
                .id(id)
                .name("Test Supplier")
                .contactPerson("John Doe")
                .phone("123456789")
                .email("test@example.com")
                .address("123 Main St")
                .active(true)
                .build();
    }

    @Test
    void testCreateSupplier() {
        when(supplierService.createSupplier(any(SupplierDTO.class))).thenReturn(supplierDTO);

        ResponseEntity<SupplierDTO> response = supplierController.createSupplier(supplierDTO);

        assertEquals(supplierDTO, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(supplierService).createSupplier(supplierDTO);
    }

    @Test
    void testGetAllSuppliers() {
        List<SupplierDTO> list = Collections.singletonList(supplierDTO);
        when(supplierService.getAllSuppliers()).thenReturn(list);

        ResponseEntity<List<SupplierDTO>> response = supplierController.getAllSuppliers();

        assertEquals(list, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(supplierService).getAllSuppliers();
    }

    @Test
    void testGetSupplierById() {
        when(supplierService.getSupplierById(id)).thenReturn(supplierDTO);

        ResponseEntity<SupplierDTO> response = supplierController.getSupplierById(id);

        assertEquals(supplierDTO, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(supplierService).getSupplierById(id);
    }

    @Test
    void testUpdateSupplier() {
        when(supplierService.updateSupplier(eq(id), any(SupplierDTO.class))).thenReturn(supplierDTO);

        ResponseEntity<SupplierDTO> response = supplierController.updateSupplier(id, supplierDTO);

        assertEquals(supplierDTO, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(supplierService).updateSupplier(id, supplierDTO);
    }

    @Test
    void testDeleteSupplier() {
        doNothing().when(supplierService).deleteSupplier(id);

        ResponseEntity<Void> response = supplierController.deleteSupplier(id);

        assertEquals(204, response.getStatusCodeValue());
        verify(supplierService).deleteSupplier(id);
    }
}