package de.uniks.pmws2324.nopm.controller;

import de.uniks.pmws2324.nopm.App;
import de.uniks.pmws2324.nopm.Main;
import de.uniks.pmws2324.nopm.model.User;
import de.uniks.pmws2324.nopm.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController extends Controller {
    public TextField nicknameField;
    public Button loginButton;
    private Parent parent;

    public LoginController(App app, Service service) {
        super(app, service);
    }

    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Login.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            this.parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Parent render() {
        if (service.getUser() != null) {
            nicknameField.setText(service.getUser().getNickname());
            if (service.getUser().getNickname().length() < 4) {
                loginButton.setDisable(true);
            }
        }
        loginButton.setOnAction(this::login);

        nicknameField.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(nicknameField.getText().length() < 4));
        return parent;
    }

    private void login(ActionEvent actionEvent) {
        service.setUser(new User().setNickname(nicknameField.getText()));
        System.out.println("Login " + service.getUser().getNickname());
        app.showAppScreen();
    }
}
