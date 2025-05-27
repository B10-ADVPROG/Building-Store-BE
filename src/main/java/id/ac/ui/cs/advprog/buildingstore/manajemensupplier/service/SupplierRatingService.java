package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class SupplierRatingService {

    private final WebClient webClient;
    private final String baseUrl;

    @Autowired
    public SupplierRatingService(WebClient webClient, @Value("${supplier.rating.api.url:https://api.example.com}") String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    public Mono<Double> getSupplierRating(String supplierName) {
        if (supplierName == null || supplierName.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Supplier name cannot be null or empty"));
        }

        return webClient.get()
                .uri(baseUrl + "/ratings/" + supplierName)
                .retrieve()
                .bodyToMono(Double.class)
                .onErrorReturn(0.0); // Default rating if service fails
    }
}