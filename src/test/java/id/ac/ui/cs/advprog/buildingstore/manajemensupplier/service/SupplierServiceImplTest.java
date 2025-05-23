package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.factory.SupplierFactory;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierFactory supplierFactory;
    
    @Mock
    private SupplierRatingService supplierRatingService;

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

        Mono<SupplierDTO> result = supplierService.createSupplier(supplierDTO);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getName().equals(supplierDTO.getName()))
                .verifyComplete();
                
        verify(supplierFactory).createSupplier(any(SupplierDTO.class));
    }

    @Test
    void shouldGetAllSuppliers() {
        when(supplierRepository.findAll()).thenReturn(Collections.singletonList(supplier));

        Flux<SupplierDTO> result = supplierService.getAllSuppliers();

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
                
        verify(supplierFactory).createSupplierDTO(any(Supplier.class));
    }

    @Test
    void shouldGetSupplierById() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.of(supplier));

        Mono<SupplierDTO> result = supplierService.getSupplierById(id);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getName().equals(supplierDTO.getName()))
                .verifyComplete();
                
        verify(supplierFactory).createSupplierDTO(any(Supplier.class));
    }

    @Test
    void shouldThrowExceptionWhenSupplierNotFound() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Mono<SupplierDTO> result = supplierService.getSupplierById(id);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
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

        Mono<SupplierDTO> result = supplierService.updateSupplier(id, updatedDTO);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto != null)
                .verifyComplete();
                
        verify(supplierRepository).save(any(Supplier.class));
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

        Mono<SupplierDTO> result = supplierService.updateSupplier(id, updatedDTO);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void shouldDeleteSupplier() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        Mono<Void> result = supplierService.deleteSupplier(id);

        StepVerifier.create(result)
                .verifyComplete();
                
        verify(supplierRepository).save(any(Supplier.class));
    }

    @Test
    void shouldThrowExceptionWhenDeleteSupplierNotFound() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Mono<Void> result = supplierService.deleteSupplier(id);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }
    
    @Test
    void shouldGetSupplierWithRating() {
        when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.of(supplier));
        when(supplierRatingService.getSupplierRating(anyString())).thenReturn(Mono.just(4.5));

        Mono<SupplierDTO> result = supplierService.getSupplierWithRating(id);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getName().equals(supplierDTO.getName()))
                .verifyComplete();
    }
}