package com.example.carrentalfrontend.frontend.models.rentalrequest;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class RentalRequestModel {
    long userId;
    long carId;
    LocalDate from;
    LocalDate to;
    String location;
    List<Long> optionalEquipmentIds;

    @JsonCreator
    public RentalRequestModel(long userId, long carId, LocalDate from, LocalDate to, String location, List<Long> optionalEquipmentIds) {
        this.userId = userId;
        this.carId = carId;
        this.from = from;
        this.to = to;
        this.location = location;
        this.optionalEquipmentIds = optionalEquipmentIds;
    }
}
