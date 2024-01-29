package de.uniks.pmws2324.nopm.controller;

import de.uniks.pmws2324.nopm.App;
import de.uniks.pmws2324.nopm.Main;
import de.uniks.pmws2324.nopm.model.Message;
import de.uniks.pmws2324.nopm.model.Topic;
import de.uniks.pmws2324.nopm.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ChatController extends Controller {

    public TextField messageField;
    public Button sendButton;
    public VBox messageBox;
    private Parent parent;
    private Topic topic;

    public ChatController(App app, Service service, Topic topic) {
        super(app, service);
        this.topic = topic;
    }

    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Chat.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            this.parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Parent render() {
        messageField.setText("");
        sendButton.setOnAction(this::sendMessage);
        sendButton.setDisable(true);
        messageField.textProperty().addListener((observable, oldValue, newValue) -> sendButton.setDisable(messageField.getText().length() < 1));
        topic.getMessages().forEach(message -> {
            System.out.println("Message: " + message.getBody());
            MessageController messageController = new MessageController(app, service, message);
            messageController.init();
            messageBox.getChildren().add(messageController.render());
        });
        // Todo richtig anmelden
        topic.listeners().addPropertyChangeListener(Topic.PROPERTY_MESSAGES, this::onMessage);
        return parent;
    }

    private void onMessage(PropertyChangeEvent propertyChangeEvent) {
        Message message = (Message) propertyChangeEvent.getNewValue();
        MessageController messageController = new MessageController(app, service, message);
        messageController.init();
        messageBox.getChildren().add(messageController.render());
    }

    private void sendMessage(ActionEvent actionEvent) {
        Instant timestamp = Instant.now();
        Message message = new Message();
        message.setBody(messageField.getText())
                .setSender(service.getUser())
                .setTimestamp(timestamp);
        topic.withMessages(message);
        messageField.setText("");
    }
}
