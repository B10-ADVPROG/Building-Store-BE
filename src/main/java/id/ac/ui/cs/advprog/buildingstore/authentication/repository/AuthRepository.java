package id.ac.ui.cs.advprog.buildingstore.authentication.repository;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthRepository {
    private List<User> userData = new ArrayList<>();

    public User save(User user) {
        userData.add(user);
        return user;
    }

    public User findByEmail(String email) {
        return userData.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

}
