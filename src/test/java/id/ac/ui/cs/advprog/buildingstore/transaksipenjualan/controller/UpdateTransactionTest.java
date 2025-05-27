package id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.transaksipenjualan.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class UpdateTransactionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    private String validToken;

    private String existingId;
    private String nonExistingId;
    private String wrongStatusId;

    @BeforeEach
    public void setUp() {
        validToken = "Bearer Token";
        existingId = "trx-123";
        nonExistingId = "trx-404";
        wrongStatusId = "trx-999";

        doNothing().when(transactionService).update(existingId);

        doThrow(new IllegalArgumentException("Transaction with id " + nonExistingId + " not found"))
                .when(transactionService).update(nonExistingId);

        doThrow(new IllegalArgumentException("Only transactions with status IN_PROGRESS can be updated"))
                .when(transactionService).update(wrongStatusId);
    }

    @Test
    public void testUpdateTransactionSuccess() throws Exception {
        mockMvc.perform(put("/transaction/update/" + existingId)
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transaction status updated successfully"));
    }

    @Test
    public void testUpdateTransactionNotFound() throws Exception {
        mockMvc.perform(put("/transaction/update/" + nonExistingId)
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction with id " + nonExistingId + " not found"));
    }

    @Test
    public void testUpdateTransactionWrongStatus() throws Exception {
        mockMvc.perform(put("/transaction/update/" + wrongStatusId)
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Only transactions with status IN_PROGRESS can be updated"));
    }

}
