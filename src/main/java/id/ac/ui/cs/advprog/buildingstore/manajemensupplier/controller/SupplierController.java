package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthorizationService;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class SupplierController {

    private final SupplierService supplierService;
    private final AuthorizationService authorizationService;

    @Autowired
    public SupplierController(SupplierService supplierService, AuthorizationService authorizationService) {
        this.supplierService = supplierService;
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> createSupplier(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody SupplierDTO supplierDTO) {
        
        return checkAdminAuthorization(authorizationHeader)
                .flatMap(authorized -> {
                    if (!authorized) {
                        return Mono.just(createUnauthorizedResponse());
                    }
                    return supplierService.createSupplier(supplierDTO)
                            .map(supplier -> ResponseEntity.ok((Object) supplier))
                            .onErrorReturn(ResponseEntity.badRequest().build());
                });
    }

    @GetMapping
    public Mono<ResponseEntity<Object>> getAllSuppliers(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        
        return checkAdminAuthorization(authorizationHeader)
                .flatMap(authorized -> {
                    if (!authorized) {
                        return Mono.just(createUnauthorizedResponse());
                    }
                    return supplierService.getAllSuppliers()
                            .collectList()
                            .map(suppliers -> ResponseEntity.ok((Object) suppliers));
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> getSupplierById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable UUID id) {
        
        return checkAdminAuthorization(authorizationHeader)
                .flatMap(authorized -> {
                    if (!authorized) {
                        return Mono.just(createUnauthorizedResponse());
                    }
                    return supplierService.getSupplierById(id)
                            .map(supplier -> ResponseEntity.ok((Object) supplier))
                            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> updateSupplier(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable UUID id, 
            @RequestBody SupplierDTO supplierDTO) {
        
        return checkAdminAuthorization(authorizationHeader)
                .flatMap(authorized -> {
                    if (!authorized) {
                        return Mono.just(createUnauthorizedResponse());
                    }
                    return supplierService.updateSupplier(id, supplierDTO)
                            .map(supplier -> ResponseEntity.ok((Object) supplier))
                            .onErrorReturn(ResponseEntity.notFound().build());
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteSupplier(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable UUID id) {
        
        return checkAdminAuthorization(authorizationHeader)
                .flatMap(authorized -> {
                    if (!authorized) {
                        return Mono.just(createUnauthorizedResponse());
                    }
                    return supplierService.deleteSupplier(id)
                            .then(Mono.just(ResponseEntity.noContent().build()))
                            .onErrorReturn(IllegalArgumentException.class, ResponseEntity.notFound().build());
                });
    }

    @GetMapping("/{id}/rating")
    public Mono<ResponseEntity<Object>> getSupplierWithRating(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable UUID id) {
        
        return checkAdminAuthorization(authorizationHeader)
                .flatMap(authorized -> {
                    if (!authorized) {
                        return Mono.just(createUnauthorizedResponse());
                    }
                    return supplierService.getSupplierWithRating(id)
                            .map(supplier -> ResponseEntity.ok((Object) supplier))
                            .onErrorReturn(ResponseEntity.notFound().build());
                });
    }

    private Mono<Boolean> checkAdminAuthorization(String authorizationHeader) {
        // Restore original authorization code
        return Mono.fromCallable(() -> {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return false;
            }
            String token = authorizationHeader.substring(7);
            return authorizationService.authorizeAdmin(token);
        });
    }

    private ResponseEntity<Object> createUnauthorizedResponse() {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", HttpStatus.FORBIDDEN.value());
        errorBody.put("error", "Forbidden");
        errorBody.put("message", "Access denied. Administrator role required for supplier operations.");
        errorBody.put("path", "/api/suppliers");
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody);
    }
}