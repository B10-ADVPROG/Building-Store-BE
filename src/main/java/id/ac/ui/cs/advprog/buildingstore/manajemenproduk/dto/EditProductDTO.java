package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditProductDTO {
    private String newProductName;
    private String newProductDescription;
    private Integer newProductPrice;
    private Integer newProductStock;
}
