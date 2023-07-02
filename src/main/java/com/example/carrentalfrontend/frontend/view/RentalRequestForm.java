package com.example.carrentalfrontend.frontend.view;

import com.example.carrentalfrontend.frontend.City;
import com.example.carrentalfrontend.frontend.client.CarClient;
import com.example.carrentalfrontend.frontend.client.EquipmentClient;
import com.example.carrentalfrontend.frontend.client.ExchangeRatesClient;
import com.example.carrentalfrontend.frontend.client.RentalClient;
import com.example.carrentalfrontend.frontend.models.car.CarModel;
import com.example.carrentalfrontend.frontend.models.rentalrequest.RentalRequestModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RentalRequestForm extends FormLayout {

    private MainView mainView;
    private CarClient carClient;
    private RentalClient rentalClient;
    private RentalForm rentalForm;
    private EquipmentClient equipmentClient;
    private ExchangeRatesClient exchangeRatesClient;

    private ComboBox<City> location = new ComboBox<>("Location");
    private DatePicker from = new DatePicker("From");
    private DatePicker to = new DatePicker("To");
    private Button checkAvailability = new Button("Check");
    private Button rent = new Button("Rent");
    private Grid<CarModel> carModelGrid = new Grid<>(CarModel.class);

    public RentalRequestForm(MainView mainView, CarClient carClient, RentalClient rentalClient,
                             ExchangeRatesClient exchangeRatesClient,
                             EquipmentClient equipmentClient) {
        this.mainView = mainView;
        this.carClient = carClient;
        this.rentalClient = rentalClient;
        this.exchangeRatesClient = exchangeRatesClient;
        this.equipmentClient = equipmentClient;

        rent.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        rent.setVisible(false);
        rent.addClickListener(e -> rentACar());
        carModelGrid.setColumns("id", "brand", "model", "type", "price", "tankCapacity", "location");
        carModelGrid.setMaxHeight("300px");
        carModelGrid.setMaxWidth("1000px");
        location.setItems(City.values());
        checkAvailability.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        checkAvailability.setMaxWidth("100px");
        checkAvailability.addClickListener(e -> findCars());
        HorizontalLayout dates = new HorizontalLayout(from, to, location);
        dates.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        HorizontalLayout checkButton = new HorizontalLayout(checkAvailability);
        checkButton.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        VerticalLayout formLayout = new VerticalLayout(dates, checkButton);
        formLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        formLayout.setPadding(false);
        formLayout.setSpacing(true);

        carModelGrid.asSingleSelect().addValueChangeListener(event -> {
            CarModel selectedCar = event.getValue();
            if (selectedCar != null) {
                showRentalCostForm(calculateCost());
                add(rent);
                rent.setVisible(true);
            }
        });

        carModelGrid.setVisible(false);
        add(formLayout, carModelGrid);
    }

    public void findCars() {
        LocalDate rentFrom = from.getValue();
        LocalDate rentTo = to.getValue();
        String chosenLocation = location.getValue().toString().toLowerCase();
        if (rentFrom != null && rentTo != null) {
            if (rentFrom.isAfter(LocalDate.now()) && rentTo.isAfter(rentFrom)) {
                List<CarModel> carsFound = carClient.carsAvailableInAGivenPeriod(rentFrom, rentTo, chosenLocation);
                if (!carsFound.isEmpty()) {
                    carModelGrid.setItems(carsFound);
                    carModelGrid.setVisible(true);
                    mainView.updateFilteredCars(carsFound);
                    calculateCost();
                } else {
                    carModelGrid.setVisible(false);
                    mainView.updateFilteredCars(Collections.emptyList());
                    Notification.show("There is no available car for the provided period!", 3000, Notification.Position.TOP_CENTER);
                }
            } else {
                carModelGrid.setVisible(false);
                mainView.updateFilteredCars(Collections.emptyList());
                Notification.show("Invalid date range! Please select a valid range.", 3000, Notification.Position.TOP_CENTER);
            }
        } else {
            carModelGrid.setVisible(false);
            mainView.updateFilteredCars(Collections.emptyList());
            Notification.show("Please choose a date range.", 3000, Notification.Position.TOP_CENTER);
        }
    }

    public BigDecimal calculateCost() {
        LocalDate rentFrom = from.getValue();
        LocalDate rentTo = to.getValue();
        GridSingleSelectionModel<CarModel> selectionModel = (GridSingleSelectionModel<CarModel>) carModelGrid.getSelectionModel();
        Optional<CarModel> selectedCar = selectionModel.getSelectedItem();
        return selectedCar
                .map(carModel -> rentalClient.calculateBasicCost(carModel.getId(), rentFrom, rentTo).setScale(2, RoundingMode.HALF_UP))
                .orElse(BigDecimal.ZERO);
    }

    public void showRentalCostForm(BigDecimal cost) {
        if (rentalForm != null) {
            remove(rentalForm);
        }
        rentalForm = new RentalForm(cost, equipmentClient, exchangeRatesClient);
        add(rentalForm);
    }

    public void rentACar() {
        LocalDate rentFrom = from.getValue();
        LocalDate rentTo = to.getValue();
        GridSingleSelectionModel<CarModel> selectionModel = (GridSingleSelectionModel<CarModel>) carModelGrid.getSelectionModel();
        Optional<CarModel> selectedCar = selectionModel.getSelectedItem();
        selectedCar.ifPresent(carModel -> rentalClient.rent(new RentalRequestModel(58L, carModel.getId(), rentFrom, rentTo,
                location.getValue().toString(), rentalForm.getSelectedEquipmentIds())));
    }


}