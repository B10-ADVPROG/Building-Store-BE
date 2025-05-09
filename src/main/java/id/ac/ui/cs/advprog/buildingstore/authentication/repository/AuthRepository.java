package id.ac.ui.cs.advprog.buildingstore.authentication.repository;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}

