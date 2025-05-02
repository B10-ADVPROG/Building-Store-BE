package id.ac.ui.cs.advprog.buildingstore.authentication.model;

import jakarta.persistence.*;

@MappedSuperclass
public class User {
    @Id
    private String email;
    private String fullname;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMINISTRATOR,
        KASIR
    }

    public User(String email, String fullname, String password, Role role) {
        setEmail(email);
        setFullname(fullname);
        setPassword(password);
        setRole(role);
    }


    // Getter and Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        if (fullname.isEmpty()) {
            throw new IllegalArgumentException("Fullname cannot be empty");
        }
        this.fullname = fullname;
    }

    public String getRole() {
        return role.name();
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
