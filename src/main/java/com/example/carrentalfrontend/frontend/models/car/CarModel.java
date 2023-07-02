package com.example.carrentalfrontend.frontend.models.car;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CarModel {
    @JsonProperty("id")
    private final long id;
    @JsonProperty("brand")
    private final String brand;
    @JsonProperty("model")
    private final String model;
    @JsonProperty("type")
    private final String type;
    @JsonProperty("tankCapacity")
    private final int tankCapacity;
    @JsonProperty("price")
    private final BigDecimal price;
    @JsonProperty("location")
    private final String location;

    @JsonCreator
    public CarModel(long id, String brand, String model, String type, int tankCapacity, BigDecimal price, String location) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.tankCapacity = tankCapacity;
        this.price = price;
        this.location = location;
    }
}

