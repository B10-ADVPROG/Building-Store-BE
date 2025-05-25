package id.ac.ui.cs.advprog.buildingstore.manajemenproduk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditProductDTO {
    private String productName;
    private String productDescription;
    private Integer productPrice;
    private Integer productStock;
}
