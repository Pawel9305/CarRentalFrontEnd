package com.example.carrentalfrontend.frontend.client;

import com.example.carrentalfrontend.frontend.apiendpoints.EquipmentApiEndpoints;
import com.example.carrentalfrontend.frontend.models.equipment.AdditionalEquipmentModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
public class EquipmentClient {

    @Value("${rental.api.url}")
    private String rentalUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public List<AdditionalEquipmentModel> getAllElements() {
        String url = rentalUrl + EquipmentApiEndpoints.FIND_ALL;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String json = response.getBody();

        try{
            AdditionalEquipmentModel[] equipmentModels = objectMapper.readValue(json, AdditionalEquipmentModel[].class);
            return Arrays.asList(equipmentModels);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
