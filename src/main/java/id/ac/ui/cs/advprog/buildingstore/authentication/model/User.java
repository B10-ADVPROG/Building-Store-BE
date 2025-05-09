package id.ac.ui.cs.advprog.buildingstore.authentication.model;

import jakarta.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "app_user")
public class User {
    @Id
    private String email;
    private String fullname;
    private String password;
    private UserRole role;

    public User(String email, String fullname, String password, String role) {
        setEmail(email);
        setFullname(fullname);
        setPassword(password);
        setRole(role);
    }

    public User() {
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
        return role.getDisplayName();
    }

    public void setRole(String role) {
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }
        this.role = UserRole.fromString(role);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
