package id.ac.ui.cs.advprog.buildingstore.authentication.factory;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.*;

public class UserFactory {

    public static User createUser(String role, String email, String fullname, String password) {
        switch (role.toLowerCase()) {
            case "kasir":
                return new Kasir(email, fullname, password);
            case "administrator":
                return new Administrator(email, fullname, password);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
