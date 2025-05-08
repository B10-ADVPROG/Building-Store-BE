package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SupplierRatingServiceTest {

    @Test
    void testGetSupplierRating() {
        WebClient webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        SupplierRatingService service = new SupplierRatingService(webClient);

        when(webClient.get().uri(any(String.class)).retrieve().bodyToMono(Double.class))
                .thenReturn(Mono.just(4.5));

        Double result = service.getSupplierRating("SupplierA").block();

        assertEquals(4.5, result);
    }
}