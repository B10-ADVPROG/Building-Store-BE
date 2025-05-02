package id.ac.ui.cs.advprog.buildingstore.authentication.repository;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
