package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    private UUID id;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private boolean active;
}