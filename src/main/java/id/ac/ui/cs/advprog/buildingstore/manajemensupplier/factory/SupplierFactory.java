package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.factory;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.model.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierFactory {

    public Supplier createSupplier(SupplierDTO supplierDTO) {
        return Supplier.builder()
                .id(supplierDTO.getId())
                .name(supplierDTO.getName())
                .contactPerson(supplierDTO.getContactPerson())
                .phone(supplierDTO.getPhone())
                .email(supplierDTO.getEmail())
                .address(supplierDTO.getAddress())
                .active(true) // Default to active when creating new supplier
                .build();
    }

    public SupplierDTO createSupplierDTO(Supplier supplier) {
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
}