package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Product {
    private String productId;
    private String name;
    private int price;
    private int stock;
}