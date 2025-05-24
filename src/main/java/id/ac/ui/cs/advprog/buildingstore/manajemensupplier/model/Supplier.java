package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "suppliers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    
    @Id
    private UUID id;  // Remove @GeneratedValue since we're setting IDs manually
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "contact_person")
    private String contactPerson;
    
    private String phone;
    
    private String email;
    
    private String address;
    
    @Column(nullable = false)
    private boolean active = true;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}