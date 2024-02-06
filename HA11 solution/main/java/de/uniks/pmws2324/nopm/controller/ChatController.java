package de.uniks.pmws2324.nopm.controller;

import de.uniks.pmws2324.nopm.App;
import de.uniks.pmws2324.nopm.Main;
import de.uniks.pmws2324.nopm.model.Message;
import de.uniks.pmws2324.nopm.model.Topic;
import de.uniks.pmws2324.nopm.service.ChatService;
import de.uniks.pmws2324.nopm.service.Service;
import javafx.application.Platform;
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
import java.util.Timer;
import java.util.TimerTask;

public class ChatController extends Controller {

    private final ChatService chatService;
    public TextField messageField;
    public Button sendButton;
    public VBox messageBox;
    private Parent parent;
    private Topic topic;
    private Instant lastFetchTimestamp;
    Timer timer = new Timer();

    public ChatController(App app, Service service, Topic topic, ChatService chatService) {
        super(app, service);
        this.topic = topic;
        this.chatService = chatService;
    }

    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Chat.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            this.parent = loader.load();
            fetchMessages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Parent render() {
        messageField.setText("");
        sendButton.setOnAction(this::sendMessage);
        sendButton.setDisable(true);
        // Todo remove Listener
        messageField.textProperty().addListener((observable, oldValue, newValue) -> sendButton.setDisable(messageField.getText().length() < 1));
        topic.getMessages().forEach(message -> {
            MessageController messageController = new MessageController(app, service, message);
            messageController.init();
            messageBox.getChildren().add(messageController.render());
        });
        // Todo remove Listener
        topic.listeners().addPropertyChangeListener(Topic.PROPERTY_MESSAGES, this::onMessage);
        return parent;
    }

    private void fetchMessages() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fetchMessages();
            }
        }, 5000);
        chatService.loadMessages(topic, lastFetchTimestamp);
        lastFetchTimestamp = Instant.now();
    }

    private void onMessage(PropertyChangeEvent propertyChangeEvent) {
        Message message = (Message) propertyChangeEvent.getNewValue();
        MessageController messageController = new MessageController(app, service, message);
        messageController.init();
        Platform.runLater(() -> messageBox.getChildren().add(messageController.render()));
    }

    private void sendMessage(ActionEvent actionEvent) {
        Message message = chatService.sendMessage(topic, service.getUser(), messageField.getText());
        messageField.setText("");
    }

    @Override
    public void destroy() {
        super.destroy();
         timer.cancel();
         timer.purge();
    }
}
