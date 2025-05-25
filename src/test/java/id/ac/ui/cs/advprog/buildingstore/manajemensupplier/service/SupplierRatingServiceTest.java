package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class SupplierRatingServiceTest {

    private SupplierRatingService service;
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        String baseUrl = "https://api.example.com";
        service = new SupplierRatingService(webClient, baseUrl);
    }

    @Test
    void testGetSupplierRating() {
        when(webClient.get().uri(any(String.class)).retrieve().bodyToMono(Double.class))
                .thenReturn(Mono.just(4.5));

        Mono<Double> result = service.getSupplierRating("SupplierA");

        StepVerifier.create(result)
                .expectNext(4.5)
                .verifyComplete();
    }

    @Test
    void testGetSupplierRatingWithNullName() {
        Mono<Double> result = service.getSupplierRating(null);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void testGetSupplierRatingWithEmptyName() {
        Mono<Double> result = service.getSupplierRating("");

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void testGetSupplierRatingHandlesError() {
        when(webClient.get().uri(any(String.class)).retrieve().bodyToMono(Double.class))
                .thenReturn(Mono.error(new RuntimeException("API error")));

        Mono<Double> result = service.getSupplierRating("SupplierA");

        StepVerifier.create(result)
                .expectNext(0.0) // Should return default value on error
                .verifyComplete();
    }

    @Test
    void testGetSupplierRatingWithValidName() {
        when(webClient.get().uri(any(String.class)).retrieve().bodyToMono(Double.class))
                .thenReturn(Mono.just(3.8));

        Mono<Double> result = service.getSupplierRating("ABC Building Supplies");

        StepVerifier.create(result)
                .expectNext(3.8)
                .verifyComplete();
    }
}