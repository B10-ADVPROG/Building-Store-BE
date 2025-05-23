package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.controller;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
        when(supplierService.createSupplier(any(SupplierDTO.class))).thenReturn(Mono.just(supplierDTO));

        Mono<SupplierDTO> result = supplierController.createSupplier(supplierDTO);
        
        StepVerifier.create(result)
            .expectNext(supplierDTO)
            .verifyComplete();
            
        verify(supplierService).createSupplier(any(SupplierDTO.class));
    }

    @Test
    void testGetAllSuppliers() {
        when(supplierService.getAllSuppliers()).thenReturn(Flux.just(supplierDTO));

        Flux<SupplierDTO> result = supplierController.getAllSuppliers();

        StepVerifier.create(result)
            .expectNext(supplierDTO)
            .verifyComplete();
            
        verify(supplierService).getAllSuppliers();
    }

    @Test
    void testGetSupplierById() {
        when(supplierService.getSupplierById(id)).thenReturn(Mono.just(supplierDTO));

        Mono<SupplierDTO> result = supplierController.getSupplierById(id);

        StepVerifier.create(result)
            .expectNext(supplierDTO)
            .verifyComplete();
            
        verify(supplierService).getSupplierById(id);
    }

    @Test
    void testUpdateSupplier() {
        when(supplierService.updateSupplier(eq(id), any(SupplierDTO.class))).thenReturn(Mono.just(supplierDTO));

        Mono<SupplierDTO> result = supplierController.updateSupplier(id, supplierDTO);

        StepVerifier.create(result)
            .expectNext(supplierDTO)
            .verifyComplete();
            
        verify(supplierService).updateSupplier(eq(id), any(SupplierDTO.class));
    }

    @Test
    void testDeleteSupplier() {
        when(supplierService.deleteSupplier(id)).thenReturn(Mono.empty());

        Mono<Void> result = supplierController.deleteSupplier(id);

        StepVerifier.create(result)
            .verifyComplete();
            
        verify(supplierService).deleteSupplier(id);
    }
    
    @Test
    void testGetSupplierWithRating() {
        when(supplierService.getSupplierWithRating(id)).thenReturn(Mono.just(supplierDTO));
        
        Mono<SupplierDTO> result = supplierController.getSupplierWithRating(id);
        
        StepVerifier.create(result)
            .expectNext(supplierDTO)
            .verifyComplete();
            
        verify(supplierService).getSupplierWithRating(id);
    }
}