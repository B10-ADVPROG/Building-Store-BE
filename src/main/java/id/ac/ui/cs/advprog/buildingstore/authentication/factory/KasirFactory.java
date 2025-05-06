package id.ac.ui.cs.advprog.buildingstore.authentication.factory;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.Kasir;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;

public class KasirFactory extends UserFactory {
    @Override
    public User createUser(String email, String fullname, String password) {
        return new Kasir(email, fullname, password);
    }
