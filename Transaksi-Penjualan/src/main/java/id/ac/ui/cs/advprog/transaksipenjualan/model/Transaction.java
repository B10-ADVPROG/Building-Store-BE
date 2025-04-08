package id.ac.ui.cs.advprog.transaksipenjualan.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class Transaction {
    private String transactionId;
    private Date date;
    private int totalAmount;
    private boolean status;
}