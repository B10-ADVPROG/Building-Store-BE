package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
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

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = convertToEntity(supplierDTO);
        supplier.setActive(true);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return convertToDTO(savedSupplier);
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDTO getSupplierById(UUID id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + id));
        return convertToDTO(supplier);
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
        return convertToDTO(updatedSupplier);
    }

    @Override
    public void deleteSupplier(UUID id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + id));
        // Soft delete
        supplier.setActive(false);
        supplierRepository.save(supplier);
    }

    private SupplierDTO convertToDTO(Supplier supplier) {
        return SupplierDTO.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .contactPerson(supplier.getContactPerson())
                .phone(supplier.getPhone())
                .email(supplier.getEmail())
                .address(supplier.getAddress())
                .active(supplier.isActive())
                .build();
    }

    private Supplier convertToEntity(SupplierDTO supplierDTO) {
        return Supplier.builder()
                .id(supplierDTO.getId())
                .name(supplierDTO.getName())
                .contactPerson(supplierDTO.getContactPerson())
                .phone(supplierDTO.getPhone())
                .email(supplierDTO.getEmail())
                .address(supplierDTO.getAddress())
                .active(supplierDTO.isActive())
                .build();
    }
}