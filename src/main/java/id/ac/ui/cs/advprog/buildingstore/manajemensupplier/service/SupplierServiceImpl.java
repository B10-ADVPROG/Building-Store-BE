package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.factory.SupplierFactory;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierFactory supplierFactory;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierFactory supplierFactory) {
        this.supplierRepository = supplierRepository;
        this.supplierFactory = supplierFactory;
    }

    @Override
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = supplierFactory.createSupplier(supplierDTO);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierFactory.createSupplierDTO(savedSupplier);
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(supplierFactory::createSupplierDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDTO getSupplierById(UUID id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + id));
        return supplierFactory.createSupplierDTO(supplier);
    }

    @Override
    public SupplierDTO updateSupplier(UUID id, SupplierDTO supplierDTO) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + id));

        supplier.setName(supplierDTO.getName());
        supplier.setContactPerson(supplierDTO.getContactPerson());
        supplier.setPhone(supplierDTO.getPhone());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setAddress(supplierDTO.getAddress());

        Supplier updatedSupplier = supplierRepository.save(supplier);
        return supplierFactory.createSupplierDTO(updatedSupplier);
    }

    @Override
    public void deleteSupplier(UUID id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + id));
        // Soft delete
        supplier.setActive(false);
        supplierRepository.save(supplier);
    }
}