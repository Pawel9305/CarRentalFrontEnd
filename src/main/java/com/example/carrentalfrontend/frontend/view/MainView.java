package com.example.carrentalfrontend.frontend.view;

import com.example.carrentalfrontend.frontend.client.*;
import com.example.carrentalfrontend.frontend.models.car.CarModel;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route("")
@PageTitle("Main page | Car Rental")
public class MainView extends VerticalLayout {

    private Grid<CarModel> grid = new Grid<>(CarModel.class);
    private CarSearchForm carSearchForm;
    private RentalRequestForm requestForm;
    private CarClient carClient;
    private UserClient userClient;
    private RentalClient rentalClient;
    private UserRegisterForm userRegisterForm;
    private RentalForm rentalForm;
    private EquipmentClient equipmentClient;
    private ExchangeRatesClient exchangeRatesClient;
    private WeatherClient weatherClient;
    private WeatherView weatherView;

    public MainView(CarClient carClient, UserClient userClient, RentalClient rentalClient,
                    EquipmentClient equipmentClient, ExchangeRatesClient exchangeRatesClient,
                    WeatherClient weatherClient) {
        this.carClient = carClient;
        this.userClient = userClient;
        this.rentalClient = rentalClient;
        this.equipmentClient = equipmentClient;
        this.exchangeRatesClient = exchangeRatesClient;
        this.weatherClient = weatherClient;

        requestForm = new RentalRequestForm(this, carClient, rentalClient, exchangeRatesClient,
                equipmentClient);
        carSearchForm = new CarSearchForm(this, carClient);
        userRegisterForm = new UserRegisterForm(this, userClient);
        weatherView = new WeatherView(this, weatherClient);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        H1 header = new H1("CAR-RENTAL");
        add(header);
        grid.setColumns("brand", "model", "type", "tankCapacity", "price", "location");
        refreshGrid();

        Tab register = new Tab("Register");
        Tab showCars = new Tab("Our fleet");
        Tab rentACar = new Tab("Rent");
        Tab returnACar = new Tab("Return");
        Tab weather = new Tab("Weather");
        String eventType = "click";
        register.getElement().addEventListener(eventType, e -> {
            hideGrid();
            showRegisterUser();
            hideCarSearchForm();
            requestForm.setVisible(false);
            weatherView.setVisible(false);
        });
        showCars.getElement().addEventListener(eventType, e -> {
            hideRegister();
            showGrid();
            showCarSearchForm();
            requestForm.setVisible(false);
        });
        rentACar.getElement().addEventListener(eventType, e -> {
            hideRegister();
            hideGrid();
            hideCarSearchForm();
            requestForm.setVisible(true);
            weatherView.setVisible(false);
        });
        returnACar.getElement().addEventListener(eventType, e -> {
            hideGrid();
            hideCarSearchForm();
            hideRegister();
            requestForm.setVisible(false);
            weatherView.setVisible(false);
        });
        weather.getElement().addEventListener(eventType, e -> {
            hideGrid();
            hideCarSearchForm();
            hideRegister();
            requestForm.setVisible(false);
            weatherView.setVisible(true);

        });
        Tabs tabs = new Tabs(register, showCars, rentACar, returnACar, weather);
        requestForm.setMaxWidth("600px");
        userRegisterForm.setMaxWidth("600px");

        Div registerUserContainer = new Div(userRegisterForm);
        Div requestFormContainer = new Div(requestForm);
        add(tabs, carSearchForm, grid, requestFormContainer, registerUserContainer, weatherView);

        weatherView.setVisible(false);
        requestForm.setVisible(false);
        hideGrid();
        hideRegister();
    }

    public void showGrid() {
        grid.setVisible(true);
    }

    public void hideGrid() {
        grid.setVisible(false);
    }

    public void refreshGrid() {
        grid.setItems(carClient.getAllCars());
    }

    public void hideRegister() {
        userRegisterForm.setVisible(false);
    }

    public void showRegisterUser() {
        userRegisterForm.setVisible(true);
    }

    public void showCarSearchForm() {
        carSearchForm.setVisible(true);
    }

    public void hideCarSearchForm() {
        carSearchForm.setVisible(false);
    }

    public void updateFilteredCars(List<CarModel> filteredCars) {
        grid.setItems(filteredCars);
    }
}
