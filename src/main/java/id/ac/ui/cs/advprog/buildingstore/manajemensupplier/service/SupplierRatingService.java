package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class SupplierRatingService {

    private final WebClient webClient;

    @Autowired
    public SupplierRatingService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Double> getSupplierRating(String supplierName) {
        String url = "https://api.example.com/supplier-rating?name=" + supplierName;
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Double.class);
    }
}