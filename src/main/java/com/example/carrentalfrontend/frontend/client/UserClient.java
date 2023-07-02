package com.example.carrentalfrontend.frontend.client;

import com.example.carrentalfrontend.frontend.apiendpoints.UserApiEndpoints;
import com.example.carrentalfrontend.frontend.models.user.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserClient {

    @Value("${rental.api.url}")
    private String rentalUrl;
    private final RestTemplate restTemplate;

    public void createNewUser(String name, String surname, String idNumber, String email) {
        String url = rentalUrl + UserApiEndpoints.CREATE_USER;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UserModel userModel = new UserModel(name, surname, idNumber, email);
        HttpEntity<UserModel> requestEntity = new HttpEntity<>(userModel, headers);
        restTemplate.postForObject(url, requestEntity, Void.class);
    }


}
