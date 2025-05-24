package id.ac.ui.cs.advprog.buildingstore.authentication.model;

import jakarta.persistence.*;

@Entity
public class Kasir extends User {
    public Kasir() {
        super();
    }

    public Kasir(String email, String fullname, String password) {
        super(email, fullname, password, "Kasir");
    }
}
