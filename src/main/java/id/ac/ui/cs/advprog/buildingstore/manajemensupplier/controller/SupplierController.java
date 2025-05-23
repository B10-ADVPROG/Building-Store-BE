package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.controller;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SupplierDTO> createSupplier(@RequestBody SupplierDTO supplierDTO) {
        return supplierService.createSupplier(supplierDTO);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<SupplierDTO> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SupplierDTO> getSupplierById(@PathVariable UUID id) {
        return supplierService.getSupplierById(id);
    }

    @PutMapping("/{id}")
    public Mono<SupplierDTO> updateSupplier(@PathVariable UUID id, @RequestBody SupplierDTO supplierDTO) {
        return supplierService.updateSupplier(id, supplierDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteSupplier(@PathVariable UUID id) {
        return supplierService.deleteSupplier(id);
    }
    
    @GetMapping(value = "/{id}/with-rating", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SupplierDTO> getSupplierWithRating(@PathVariable UUID id) {
        return supplierService.getSupplierWithRating(id);
    }
}