package com.example.carrentalfrontend.frontend.forecast;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DailyModel {
    private final String time;
    private final int weatherCode;
    private final double temperature_2m_max;
    private final double temperature_2m_min;
}
