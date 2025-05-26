package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SupplierService {
    Mono<SupplierDTO> createSupplier(SupplierDTO supplierDTO);
    Flux<SupplierDTO> getAllSuppliers();
    Mono<SupplierDTO> getSupplierById(UUID id);
    Mono<SupplierDTO> updateSupplier(UUID id, SupplierDTO supplierDTO);
    Mono<Void> deleteSupplier(UUID id);
    Mono<SupplierDTO> getSupplierWithRating(UUID id);
}