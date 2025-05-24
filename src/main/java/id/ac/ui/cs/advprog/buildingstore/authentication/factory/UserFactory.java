package id.ac.ui.cs.advprog.buildingstore.authentication.factory;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.*;

public abstract class UserFactory {
    // Factory method
    public abstract User createUser(String email, String fullname, String password);
}

