package com.example.carrentalfrontend.frontend.models.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserModel {

    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("idNumber")
    private String idNumber;

    @JsonProperty("email")
    private String email;

    @JsonCreator
    public UserModel(String name, String surname, String idNumber, String email) {
        this.name = name;
        this.surname = surname;
        this.idNumber = idNumber;
        this.email = email;
    }
}
