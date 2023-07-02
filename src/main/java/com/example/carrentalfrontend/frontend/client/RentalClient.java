package com.example.carrentalfrontend.frontend.client;

import com.example.carrentalfrontend.frontend.apiendpoints.RentalApiEndpoints;
import com.example.carrentalfrontend.frontend.models.rentalrequest.RentalRequestModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RentalClient {

    @Value("${rental.api.url}")
    private String rentalUrl;
    private final RestTemplate restTemplate;

    public BigDecimal calculateBasicCost(long carId, LocalDate from, LocalDate to) {
        String calculateUrl = rentalUrl + RentalApiEndpoints.BASIC_COST;
        URI url = UriComponentsBuilder.fromHttpUrl(calculateUrl)
                .queryParam("carId", carId)
                .queryParam("from", from)
                .queryParam("to", to)
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<BigDecimal> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                BigDecimal.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void rent(final RentalRequestModel rentalRequestModel) {
        String url = rentalUrl + RentalApiEndpoints.RENTAL_ADDRESS;
        restTemplate.postForObject(url, rentalRequestModel, Void.class);
    }
}
