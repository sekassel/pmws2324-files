package de.uniks.pmws2324.nopm.controller;

import de.uniks.pmws2324.nopm.App;
import de.uniks.pmws2324.nopm.Main;
import de.uniks.pmws2324.nopm.service.ChatService;
import de.uniks.pmws2324.nopm.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

public class AppController extends Controller {

    private final ChatService chatService;
    public Button logoutButton;
    public TabPane chatTabPane;
    private Parent parent;

    public AppController(App app, Service service, ChatService chatService) {
        super(app, service);
        this.chatService = chatService;
    }
    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/App.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            this.parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Parent render() {
        this.chatService.getTopics().forEach(topic -> {
            service.addTopic(topic);
            ChatController chatController = new ChatController(app, service, topic, chatService);
            chatController.init();
            Tab tab = new Tab();
            tab.setText(topic.getTitle());
            tab.setContent(chatController.render());
            chatTabPane.getTabs().add(tab);
        });
        logoutButton.setOnAction(this::logout);
        return parent;
    }

    private void logout(ActionEvent actionEvent) {
        chatService.logout(service.getUser().getNickname());
        app.showLoginScreen();
    }
}
