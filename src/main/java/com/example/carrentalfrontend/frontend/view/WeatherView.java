package com.example.carrentalfrontend.frontend.view;

import com.example.carrentalfrontend.frontend.City;
import com.example.carrentalfrontend.frontend.client.WeatherClient;
import com.example.carrentalfrontend.frontend.models.weathermodel.DailyModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class WeatherView extends VerticalLayout {

    private ComboBox<City> cities = new ComboBox<>("Location");
    private Button getWeatherButton = new Button("Get weather");
    private MainView mainView;
    private Span weatherInfo = new Span();
    private WeatherClient weatherClient;

    public WeatherView(MainView mainView, WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
        this.mainView = mainView;

        setAlignItems(Alignment.CENTER);
        cities.setItems(City.values());
        add(cities, getWeatherButton, weatherInfo);

        cities.addValueChangeListener(e -> getWeatherButton.setVisible(true));
        getWeatherButton.addClickListener(e -> showWeatherForecast(cities.getValue().toString()));
        getWeatherButton.setVisible(false);
        getWeatherButton.getStyle().set("border", "none");
        cities.setVisible(true);
    }

    public void showWeatherForecast(String location) {
        weatherInfo.removeAll();
        List<DailyModel> forecast = weatherClient.getWeatherForecast(location.toLowerCase());
        VerticalLayout forecastLayout = new VerticalLayout();
        forecast.forEach(daily -> {
            Span date = new Span("Date: " + daily.getTime());
            Span maxTemp = new Span("Max temp: " + daily.getTemperature_2m_max());
            Span minTemp = new Span("Min temp: " + daily.getTemperature_2m_min());

            VerticalLayout dailyLayout = new VerticalLayout(date, maxTemp, minTemp);
            dailyLayout.setSpacing(false);
            dailyLayout.setMargin(false);
            dailyLayout.setPadding(false);

            forecastLayout.add(dailyLayout);
            weatherInfo.add(forecastLayout);
            weatherInfo.add(new Hr());
        });

        weatherInfo.setVisible(true);
    }
}
