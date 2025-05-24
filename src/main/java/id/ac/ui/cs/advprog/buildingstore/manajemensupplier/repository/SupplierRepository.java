package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.repository;

import id.ac.ui.cs.advprog.buildingstore.manajemensupplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    List<Supplier> findByActiveTrue();
}