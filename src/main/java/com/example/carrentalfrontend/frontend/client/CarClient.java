package com.example.carrentalfrontend.frontend.client;

import com.example.carrentalfrontend.frontend.apiendpoints.CarApiEndpoints;
import com.example.carrentalfrontend.frontend.models.car.CarModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CarClient {

    @Value("${rental.api.url}")
    private String rentalUrl;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    // v1/cars - getAllCars
    //
    public List<CarModel> getAllCars() {
        String allCarsUrl = rentalUrl + CarApiEndpoints.ALL_CARS;
        ResponseEntity<String> response = restTemplate.getForEntity(allCarsUrl, String.class);
        return parseCarModelListResponse(response);
    }

    public List<CarModel> carsAvailableInAGivenPeriod(LocalDate rentFrom, LocalDate rentTo, String chosenLocation) {
        String availableCars = rentalUrl + CarApiEndpoints.PERIOD_AVAILABLE;
        URI url = UriComponentsBuilder.fromHttpUrl(availableCars)
                .queryParam("rentFrom", rentFrom)
                .queryParam("rentTo", rentTo)
                .queryParam("chosenLocation", chosenLocation)
                .build()
                .toUri();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return parseCarModelListResponse(response);
    }

    private List<CarModel> parseCarModelListResponse(ResponseEntity<String> response) {
        String json = response.getBody();
        try {
            CarModel[] cars = objectMapper.readValue(json, CarModel[].class);
            return Arrays.asList(cars);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }


}
