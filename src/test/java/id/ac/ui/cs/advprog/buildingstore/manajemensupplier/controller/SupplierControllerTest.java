package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthorizationService;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class SupplierControllerTest {

    @Mock
    private SupplierService supplierService;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private SupplierController supplierController;

    private SupplierDTO supplierDTO;
    private UUID id;
    private String authHeader;

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
        authHeader = "Bearer valid-token";
        
        // Mock authorization to return true for admin
        when(authorizationService.authorizeAdmin("valid-token")).thenReturn(true);
    }

    @Test
    void testCreateSupplier() {
        when(supplierService.createSupplier(any(SupplierDTO.class))).thenReturn(Mono.just(supplierDTO));

        Mono<ResponseEntity<Object>> result = supplierController.createSupplier(authHeader, supplierDTO);

        StepVerifier.create(result)
                .expectNextMatches(response -> 
                    response.getStatusCode().is2xxSuccessful() && 
                    response.getBody().equals(supplierDTO))
                .verifyComplete();

        verify(supplierService).createSupplier(supplierDTO);
    }

    @Test
    void testGetAllSuppliers() {
        when(supplierService.getAllSuppliers()).thenReturn(Flux.just(supplierDTO));

        Mono<ResponseEntity<Object>> result = supplierController.getAllSuppliers(authHeader);

        StepVerifier.create(result)
                .expectNextMatches(response -> 
                    response.getStatusCode().is2xxSuccessful() && 
                    ((List<?>) response.getBody()).contains(supplierDTO))
                .verifyComplete();

        verify(supplierService).getAllSuppliers();
    }

    @Test
    void testGetSupplierById() {
        when(supplierService.getSupplierById(id)).thenReturn(Mono.just(supplierDTO));

        Mono<ResponseEntity<Object>> result = supplierController.getSupplierById(authHeader, id);

        StepVerifier.create(result)
                .expectNextMatches(response -> 
                    response.getStatusCode().is2xxSuccessful() && 
                    response.getBody().equals(supplierDTO))
                .verifyComplete();

        verify(supplierService).getSupplierById(id);
    }

    @Test
    void testGetSupplierByIdNotFound() {
        when(supplierService.getSupplierById(id)).thenReturn(Mono.empty());

        Mono<ResponseEntity<Object>> result = supplierController.getSupplierById(authHeader, id);

        StepVerifier.create(result)
                .expectNextMatches(response -> 
                    response.getStatusCode().is4xxClientError() && 
                    response.getStatusCode().value() == 404)
                .verifyComplete();

        verify(supplierService).getSupplierById(id);
    }

    @Test
    void testUpdateSupplier() {
        when(supplierService.updateSupplier(eq(id), any(SupplierDTO.class))).thenReturn(Mono.just(supplierDTO));

        Mono<ResponseEntity<Object>> result = supplierController.updateSupplier(authHeader, id, supplierDTO);

        StepVerifier.create(result)
                .expectNextMatches(response -> 
                    response.getStatusCode().is2xxSuccessful() && 
                    response.getBody().equals(supplierDTO))
                .verifyComplete();

        verify(supplierService).updateSupplier(id, supplierDTO);
    }

    @Test
    void testDeleteSupplier() {
        when(supplierService.deleteSupplier(id)).thenReturn(Mono.empty());

        Mono<ResponseEntity<Object>> result = supplierController.deleteSupplier(authHeader, id);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();

        verify(supplierService).deleteSupplier(id);
    }

    @Test
    void testUnauthorizedAccess() {
        when(authorizationService.authorizeAdmin("valid-token")).thenReturn(false);

        Mono<ResponseEntity<Object>> result = supplierController.getAllSuppliers(authHeader);

        StepVerifier.create(result)
                .expectNextMatches(response -> 
                    response.getStatusCode().value() == 403 &&
                    response.getBody() instanceof Map)
                .verifyComplete();

        verify(supplierService, never()).getAllSuppliers();
    }

    @Test
    void testGetSupplierWithRating() {
        when(supplierService.getSupplierWithRating(id)).thenReturn(Mono.just(supplierDTO));

        Mono<ResponseEntity<Object>> result = supplierController.getSupplierWithRating(authHeader, id);

        StepVerifier.create(result)
                .expectNextMatches(response -> 
                    response.getStatusCode().is2xxSuccessful() && 
                    response.getBody().equals(supplierDTO))
                .verifyComplete();

        verify(supplierService).getSupplierWithRating(id);
    }
}