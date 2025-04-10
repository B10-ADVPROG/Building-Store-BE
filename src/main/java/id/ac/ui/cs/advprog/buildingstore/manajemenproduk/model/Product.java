package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Product {
    private String productId;
    private String productName;
    private String productDescription;
    private int productPrice;
    private int productStock;
}
