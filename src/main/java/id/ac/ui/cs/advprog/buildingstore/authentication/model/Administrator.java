package id.ac.ui.cs.advprog.buildingstore.authentication.model;

public class Administrator extends User {
    public Administrator(String email, String fullname, String password) {
        super(email, fullname, password, Role.ADMINISTRATOR);
    }
}

