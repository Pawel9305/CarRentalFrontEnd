package com.example.carrentalfrontend.frontend.models.weathermodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyModel {
    private String time;
    private int weatherCode;
    private double temperature_2m_max;
    private double temperature_2m_min;
}
