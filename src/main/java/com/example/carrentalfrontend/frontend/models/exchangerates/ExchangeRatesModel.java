package com.example.carrentalfrontend.frontend.models.exchangerates;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ExchangeRatesModel {

    @JsonProperty("result")
    private BigDecimal result;
}
