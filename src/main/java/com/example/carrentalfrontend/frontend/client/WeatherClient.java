package com.example.carrentalfrontend.frontend.client;

import com.example.carrentalfrontend.frontend.apiendpoints.WeatherApiEndpoint;
import com.example.carrentalfrontend.frontend.models.weathermodel.DailyModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherClient {

    @Value("${rental.api.url}")
    private String rentalUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public List<DailyModel> getWeatherForecast(final String location) {
        String weatherUrl = rentalUrl + WeatherApiEndpoint.WEATHER_FOR7DAYS + "/" + location;
        ResponseEntity<String> response = restTemplate.getForEntity(weatherUrl, String.class);

        String json = response.getBody();

        try {
            DailyModel[] dailies = objectMapper.readValue(json, DailyModel[].class);
            return Arrays.asList(dailies);
        } catch (IOException e) {
            log.error("Could not download weather forecast");
            return Collections.emptyList();
        }
    }
}
