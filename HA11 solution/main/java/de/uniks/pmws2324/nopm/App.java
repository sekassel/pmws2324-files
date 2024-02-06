package de.uniks.pmws2324.nopm;

import de.uniks.pmws2324.nopm.controller.AppController;
import de.uniks.pmws2324.nopm.controller.ChatController;
import de.uniks.pmws2324.nopm.controller.LoginController;
import de.uniks.pmws2324.nopm.model.User;
import de.uniks.pmws2324.nopm.service.ChatApiService;
import de.uniks.pmws2324.nopm.service.ChatService;
import de.uniks.pmws2324.nopm.service.Service;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class App extends Application {
    private Stage stage;
    private Service service;
    private LoginController loginController;
    private AppController appController;
    private ChatService chatService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        service = new Service();
        chatService = new ChatService(new ChatApiService(), service);
        // start application
        showLoginScreen();
        primaryStage.show();
    }

    public void showLoginScreen() {
        cleanup();
        // show Start screen
        loginController = new LoginController(this, service, chatService);
        loginController.init();
        Scene scene = new Scene(loginController.render());

        stage.setTitle("NoPM - Login");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    public void showAppScreen() {
        cleanup();
        // show App screen
        appController = new AppController(this, service, chatService);
        appController.init();
        Scene scene = new Scene(appController.render());

        stage.setTitle("NoPM - Chat");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

    }

    private void cleanup() {
        // call cascading stop
        if (loginController != null) {
            loginController.destroy();
            loginController = null;
        }
        if (appController != null) {
            appController.destroy();
            appController = null;
        }

    }
}
