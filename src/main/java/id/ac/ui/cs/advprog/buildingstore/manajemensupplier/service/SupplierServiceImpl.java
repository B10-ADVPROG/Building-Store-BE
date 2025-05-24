package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.factory.SupplierFactory;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierFactory supplierFactory;
    private final SupplierRatingService supplierRatingService;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               SupplierFactory supplierFactory,
                               SupplierRatingService supplierRatingService) {
        this.supplierRepository = supplierRepository;
        this.supplierFactory = supplierFactory;
        this.supplierRatingService = supplierRatingService;
    }

    @Override
    public Mono<SupplierDTO> createSupplier(SupplierDTO supplierDTO) {
        return Mono.fromCallable(() -> {
            Supplier supplier = supplierFactory.createSupplier(supplierDTO);
            return supplierRepository.save(supplier);
        })
        .map(supplierFactory::createSupplierDTO)
        .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<SupplierDTO> getAllSuppliers() {
        return Flux.defer(() -> Flux.fromIterable(supplierRepository.findByActiveTrue()))
                .map(supplierFactory::createSupplierDTO)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<SupplierDTO> getSupplierById(UUID id) {
        return Mono.defer(() -> Mono.justOrEmpty(supplierRepository.findById(id)))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Supplier not found with id: " + id)))
                .map(supplierFactory::createSupplierDTO)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<SupplierDTO> updateSupplier(UUID id, SupplierDTO supplierDTO) {
        return Mono.fromCallable(() -> supplierRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + id)))
                .flatMap(supplier -> {
                    supplier.setName(supplierDTO.getName());
                    supplier.setContactPerson(supplierDTO.getContactPerson());
                    supplier.setPhone(supplierDTO.getPhone());
                    supplier.setEmail(supplierDTO.getEmail());
                    supplier.setAddress(supplierDTO.getAddress());
                    return Mono.fromCallable(() -> supplierRepository.save(supplier));
                })
                .map(supplierFactory::createSupplierDTO)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> deleteSupplier(UUID id) {
        return Mono.fromCallable(() -> supplierRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + id)))
                .flatMap(supplier -> {
                    supplier.setActive(false);
                    return Mono.fromCallable(() -> supplierRepository.save(supplier));
                })
                .then()
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<SupplierDTO> getSupplierWithRating(UUID id) {
        return getSupplierById(id)
                .flatMap(supplierDTO -> supplierRatingService.getSupplierRating(supplierDTO.getName())
                        .map(rating -> {
                            // This is where you could enhance the DTO with a rating field if needed
                            System.out.println("Supplier " + supplierDTO.getName() + " has rating: " + rating);
                            return supplierDTO;
                        })
                        .onErrorReturn(supplierDTO) // Return supplier even if rating fetch fails
                );
    }
}