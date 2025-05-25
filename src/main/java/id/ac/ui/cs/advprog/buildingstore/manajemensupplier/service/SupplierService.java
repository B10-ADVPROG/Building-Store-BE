package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;

import java.util.List;
import java.util.UUID;

public interface SupplierService {
    SupplierDTO createSupplier(SupplierDTO supplierDTO);
    List<SupplierDTO> getAllSuppliers();
    SupplierDTO getSupplierById(UUID id);
    SupplierDTO updateSupplier(UUID id, SupplierDTO supplierDTO);
    void deleteSupplier(UUID id);
}
