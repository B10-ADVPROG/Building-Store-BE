package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.factory.SupplierFactory;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.repository.SupplierRepository;
import io.micrometer.core.instrument.Timer;
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
    private final SupplierMonitoringService monitoringService;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               SupplierFactory supplierFactory,
                               SupplierRatingService supplierRatingService,
                               SupplierMonitoringService monitoringService) {
        this.supplierRepository = supplierRepository;
        this.supplierFactory = supplierFactory;
        this.supplierRatingService = supplierRatingService;
        this.monitoringService = monitoringService;
    }

    @Override
    public Mono<SupplierDTO> createSupplier(SupplierDTO supplierDTO) {
        return Mono.fromCallable(() -> {
            Timer.Sample sample = monitoringService.startTimer();
            try {
                Supplier supplier = supplierFactory.createSupplier(supplierDTO);
                Supplier savedSupplier = supplierRepository.save(supplier);
                monitoringService.recordSupplierCreation();
                updateSupplierCounts();
                return savedSupplier;
            } catch (Exception e) {
                monitoringService.recordSupplierError("create");
                throw e;
            } finally {
                monitoringService.recordOperationTime(sample, "create");
            }
        })
        .map(supplierFactory::createSupplierDTO)
        .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<SupplierDTO> getAllSuppliers() {
        return Flux.defer(() -> {
            Timer.Sample sample = monitoringService.startTimer();
            try {
                return Flux.fromIterable(supplierRepository.findByActive(true))
                        .map(supplierFactory::createSupplierDTO)
                        .doFinally(signal -> {
                            monitoringService.recordOperationTime(sample, "getAll");
                            updateSupplierCounts();
                        });
            } catch (Exception e) {
                monitoringService.recordSupplierError("getAll");
                monitoringService.recordOperationTime(sample, "getAll");
                throw e;
            }
        })
        .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<SupplierDTO> getSupplierById(UUID id) {
        return Mono.fromCallable(() -> {
            Timer.Sample sample = monitoringService.startTimer();
            try {
                return supplierRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + id));
            } catch (Exception e) {
                monitoringService.recordSupplierError("getById");
                throw e;
            } finally {
                monitoringService.recordOperationTime(sample, "getById");
            }
        })
        .map(supplierFactory::createSupplierDTO)
        .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<SupplierDTO> updateSupplier(UUID id, SupplierDTO supplierDTO) {
        return Mono.fromCallable(() -> {
            Timer.Sample sample = monitoringService.startTimer();
            try {
                Supplier supplier = supplierRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + id));

                supplier.setName(supplierDTO.getName());
                supplier.setContactPerson(supplierDTO.getContactPerson());
                supplier.setPhone(supplierDTO.getPhone());
                supplier.setEmail(supplierDTO.getEmail());
                supplier.setAddress(supplierDTO.getAddress());

                Supplier updatedSupplier = supplierRepository.save(supplier);
                monitoringService.recordSupplierUpdate();
                return updatedSupplier;
            } catch (Exception e) {
                monitoringService.recordSupplierError("update");
                throw e;
            } finally {
                monitoringService.recordOperationTime(sample, "update");
            }
        })
        .map(supplierFactory::createSupplierDTO)
        .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> deleteSupplier(UUID id) {
        return Mono.fromCallable(() -> {
            Timer.Sample sample = monitoringService.startTimer();
            try {
                Supplier supplier = supplierRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + id));
                supplier.setActive(false);
                supplierRepository.save(supplier);
                monitoringService.recordSupplierDeletion();
                updateSupplierCounts();
                return null;
            } catch (Exception e) {
                monitoringService.recordSupplierError("delete");
                throw e;
            } finally {
                monitoringService.recordOperationTime(sample, "delete");
            }
        })
        .then()
        .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<SupplierDTO> getSupplierWithRating(UUID id) {
        return getSupplierById(id)
                .flatMap(supplierDTO -> supplierRatingService.getSupplierRating(supplierDTO.getName())
                        .map(rating -> {
                            System.out.println("Supplier " + supplierDTO.getName() + " has rating: " + rating);
                            return supplierDTO;
                        })
                        .onErrorReturn(supplierDTO));
    }

    private void updateSupplierCounts() {
        long totalCount = supplierRepository.count();
        long activeCount = supplierRepository.findByActive(true).size();
        monitoringService.recordSupplierCount(totalCount);
        monitoringService.recordActiveSupplierCount(activeCount);
    }
}