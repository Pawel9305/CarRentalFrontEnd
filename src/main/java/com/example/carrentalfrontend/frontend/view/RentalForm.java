package com.example.carrentalfrontend.frontend.view;


import com.example.carrentalfrontend.frontend.client.EquipmentClient;
import com.example.carrentalfrontend.frontend.client.ExchangeRatesClient;
import com.example.carrentalfrontend.frontend.models.equipment.AdditionalEquipmentModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

@Getter
public class RentalForm extends FormLayout {
    private List<Long> selectedEquipmentIds;
    private final CheckboxGroup<AdditionalEquipmentModel> additionalEqChoice = new CheckboxGroup<>("Optional equipment");
    private final ComboBox<String> currency = new ComboBox<>("Payment currency");
    private final Button calculateAmountOfCurrency = new Button("Calculate");
    private final Span cost = new Span();
    private final Span convertedCost = new Span();
    private BigDecimal convertedAmount;
    private final BigDecimal totalCost;
    private EquipmentClient equipmentClient;
    private ExchangeRatesClient exchangeRatesClient;


    public RentalForm(BigDecimal totalCost, EquipmentClient equipmentClient, ExchangeRatesClient exchangeRatesClient) {
        this.exchangeRatesClient = exchangeRatesClient;
        this.equipmentClient = equipmentClient;
        this.totalCost = totalCost;
        convertedCost.setVisible(false);
        cost.setText("Total cost: " + totalCost.toString() + " PLN");
        additionalEqChoice.setItems(equipmentClient.getAllElements());
        additionalEqChoice.setItemLabelGenerator(equipment -> equipment.getDescription() + " - " + equipment.getPrice() + " PLN");

        additionalEqChoice.addValueChangeListener(e -> cost.setText("Total cost: " + totalCost.add(getAdditionalCost())));
        calculateAmountOfCurrency.addClickListener(e -> {
            String chosenCurrency = currency.getValue();
            convertedAmount = exchangeRatesClient.getAmountToPay("PLN", chosenCurrency, totalCost.add(getAdditionalCost())).setScale(2, RoundingMode.HALF_UP);
            convertedCost.setText("Total cost in your currency is:   " + convertedAmount);
            convertedCost.setVisible(true);
        });

        currency.setItems("PLN", "EUR", "USD", "JPY", "AUD", "CHF", "GBP");
        add(cost, additionalEqChoice, currency, calculateAmountOfCurrency, convertedCost);
    }

    public BigDecimal getAdditionalCost() {
        BigDecimal additionalEquipmentCost = BigDecimal.ZERO;
        selectedEquipmentIds = additionalEqChoice.getSelectedItems().stream()
                .map(AdditionalEquipmentModel::getId)
                .toList();

        Set<AdditionalEquipmentModel> selectedEquipment = additionalEqChoice.getSelectedItems();
        for (AdditionalEquipmentModel equipment : selectedEquipment) {
            additionalEquipmentCost = additionalEquipmentCost.add(equipment.getPrice());
        }
        return additionalEquipmentCost;
    }
}
