package com.example.carrentalfrontend.frontend.models.equipment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AdditionalEquipmentModel {
    private long id;
    private BigDecimal price;
    private String description;
}
