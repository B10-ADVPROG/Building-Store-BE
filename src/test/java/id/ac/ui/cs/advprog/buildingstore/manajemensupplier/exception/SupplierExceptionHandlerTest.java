package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SupplierExceptionHandlerTest {
    
    private SupplierExceptionHandler exceptionHandler;
    
    @BeforeEach
    void setUp() {
        exceptionHandler = new SupplierExceptionHandler();
    }
    
    @Test
    void testHandleIllegalArgumentException() {
        // Create exception
        IllegalArgumentException exception = new IllegalArgumentException("Invalid supplier data");
        
        // Handle exception
        Mono<ResponseEntity<Map<String, Object>>> result = exceptionHandler.handleIllegalArgumentException(exception);
        
        // Verify result
        StepVerifier.create(result)
            .assertNext(response -> {
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                Map<String, Object> body = response.getBody();
                assertNotNull(body);
                assertTrue(body.containsKey("timestamp"));
                assertEquals("Invalid supplier data", body.get("message"));
            })
            .verifyComplete();
    }
    
    @Test
    void testHandleGenericException() {
        // Create exception
        RuntimeException exception = new RuntimeException("Something went wrong");
        
        // Handle exception
        Mono<ResponseEntity<Map<String, Object>>> result = exceptionHandler.handleGenericException(exception);
        
        // Verify result
        StepVerifier.create(result)
            .assertNext(response -> {
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
                Map<String, Object> body = response.getBody();
                assertNotNull(body);
                assertTrue(body.containsKey("timestamp"));
                assertEquals("An error occurred: Something went wrong", body.get("message"));
            })
            .verifyComplete();
    }
}