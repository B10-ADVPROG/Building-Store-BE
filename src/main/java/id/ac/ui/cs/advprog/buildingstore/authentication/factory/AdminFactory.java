package id.ac.ui.cs.advprog.buildingstore.authentication.factory;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.Administrator;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;

public class AdminFactory extends UserFactory {
    @Override
    public User createUser(String email, String fullname, String password) {
        return new Administrator(email, fullname, password);
    }
}
