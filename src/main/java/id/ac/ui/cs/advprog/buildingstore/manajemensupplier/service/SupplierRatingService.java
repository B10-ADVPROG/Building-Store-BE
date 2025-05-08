package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class SupplierRatingService {

    private final WebClient webClient;
    private final String ratingApiBaseUrl;

    @Autowired
    public SupplierRatingService(WebClient webClient,
                                @Value("${supplier.rating.api.base-url:https://api.example.com}") String ratingApiBaseUrl) {
        this.webClient = webClient;
        this.ratingApiBaseUrl = ratingApiBaseUrl;
    }

    public Mono<Double> getSupplierRating(String supplierName) {
        String url = String.format("%s/supplier-rating?name=%s", ratingApiBaseUrl, supplierName);
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Double.class);
    }
}