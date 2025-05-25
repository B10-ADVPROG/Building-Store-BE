package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class SupplierRatingServiceTest {

    @Test
    void testGetSupplierRating() {
        WebClient webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        String baseUrl = "https://api.example.com";
        SupplierRatingService service = new SupplierRatingService(webClient, baseUrl);

        when(webClient.get().uri(any(String.class)).retrieve().bodyToMono(Double.class))
                .thenReturn(Mono.just(4.5));

        Double result = service.getSupplierRating("SupplierA").block();

        assertEquals(4.5, result);
    }

    @Test
    void testGetSupplierRatingWithNullName() {
        WebClient webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        String baseUrl = "https://api.example.com";
        SupplierRatingService service = new SupplierRatingService(webClient, baseUrl);

        when(webClient.get().uri(any(String.class)).retrieve().bodyToMono(Double.class))
                .thenReturn(Mono.just(0.0));

        Double result = service.getSupplierRating(null).block();

        assertEquals(0.0, result);
    }

    @Test
    void testGetSupplierRatingHandlesError() {
        WebClient webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        String baseUrl = "https://api.example.com";
        SupplierRatingService service = new SupplierRatingService(webClient, baseUrl);

        when(webClient.get().uri(any(String.class)).retrieve().bodyToMono(Double.class))
                .thenReturn(Mono.error(new RuntimeException("API error")));

        assertThrows(RuntimeException.class, () -> {
            service.getSupplierRating("SupplierA").block();
        });
    }
}