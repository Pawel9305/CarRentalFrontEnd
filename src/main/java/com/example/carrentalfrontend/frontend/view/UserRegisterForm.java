package com.example.carrentalfrontend.frontend.view;

import com.example.carrentalfrontend.frontend.client.UserClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;


public class UserRegisterForm extends FormLayout {

    private MainView mainView;
    private UserClient userClient;
    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private TextField idNumber = new TextField("ID number");
    private TextField email = new TextField("e-mail");
    private PasswordField password = new PasswordField("Password");
    private PasswordField confirmPassword = new PasswordField("Confirm password");

    private Button register = new Button("Register");
    private Button cancel = new Button("Cancel");

    public UserRegisterForm(MainView mainView, UserClient userClient) {
        this.mainView = mainView;
        this.userClient = userClient;
        add(name, surname, idNumber, email, password, confirmPassword);
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2));
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        register.addClickListener(e -> save());
        HorizontalLayout buttonLayout = new HorizontalLayout(cancel, register);
        buttonLayout.setAlignItems(FlexComponent.Alignment.START);
        add(buttonLayout);
    }

    private void save() {
        userClient.createNewUser(name.getValue(), surname.getValue(), idNumber.getValue(), email.getValue());
    }
}
