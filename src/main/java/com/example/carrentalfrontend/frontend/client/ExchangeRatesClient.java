package com.example.carrentalfrontend.frontend.client;

import com.example.carrentalfrontend.frontend.apiendpoints.ExchangeRatesApiEndpoints;
import com.example.carrentalfrontend.frontend.models.exchangerates.ExchangeRatesModel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExchangeRatesClient {

    @Value("${rental.api.url}")
    private String rentalUrl;
    private final RestTemplate restTemplate;

    public BigDecimal getAmountToPay(String from, String to, BigDecimal amount) {
        URI url = UriComponentsBuilder.fromHttpUrl(rentalUrl + ExchangeRatesApiEndpoints.EXCHANGE)
                .queryParam("from", from)
                .queryParam("to", to)
                .queryParam("amount", amount)
                .build()
                .toUri();

        ExchangeRatesModel response = restTemplate.getForObject(url, ExchangeRatesModel.class);
        BigDecimal result = Optional.ofNullable(response).map(ExchangeRatesModel::getResult).orElse(BigDecimal.ZERO);
        return Optional.ofNullable(result).orElse(new BigDecimal("0"));
    }
}
