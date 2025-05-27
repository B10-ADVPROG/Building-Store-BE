package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.controller;

import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class DeleteTransactionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    private String validToken;

    @BeforeEach
    void setUp() {
        validToken = "Bearer Token";
    }

    @Test
    public void testDeleteTransactionSuccess() throws Exception {
        String transactionId = "trx123";

        Mockito.doNothing().when(transactionService).deleteById(transactionId);

        mockMvc.perform(delete("/transaction/delete/{id}", transactionId)
                        .header("Authorization", validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transaction deleted successfully"));
    }

    @Test
    public void testDeleteTransactionNotFound() throws Exception {
        String transactionId = "trxNotExist";

        doThrow(new IllegalArgumentException("Transaction with id " + transactionId + " not found"))
                .when(transactionService).deleteById(transactionId);

        mockMvc.perform(delete("/transaction/delete/{id}", transactionId)
                        .header("Authorization", validToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction with id " + transactionId + " not found"));
    }

    @Test
    public void testDeleteTransactionUnauthorized() throws Exception {
        String transactionId = "trx123";

        mockMvc.perform(delete("/transaction/delete/{id}", transactionId))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or missing token"));
    }
}
