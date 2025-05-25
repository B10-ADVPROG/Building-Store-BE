package id.ac.ui.cs.advprog.buildingstore.manajemenpelanggan.model;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Customer {
    private Integer id;
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    private LocalDate registrationDate;
    
    // Constructors
    public Customer() {
        this.registrationDate = LocalDate.now();
    }

    public Customer(String name, String phoneNumber, String address, String email) {
        this();
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}