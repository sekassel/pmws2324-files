package de.uniks.pmws2324.nopm.controller;

import de.uniks.pmws2324.nopm.App;
import de.uniks.pmws2324.nopm.Main;
import de.uniks.pmws2324.nopm.model.Message;
import de.uniks.pmws2324.nopm.service.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MessageController extends Controller {

    public TextFlow messageBubble;
    public Label bodyText;
    public Label infoText;
    private Parent parent;
    private Message message;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public MessageController(App app, Service service, Message message) {
        super(app, service);
        this.message = message;
    }

    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Message.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            this.parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Parent render() {
        bodyText.setText(message.getBody());
        String time = message.getTimestamp().atZone(ZoneId.systemDefault()).format(TIME_FORMAT);
        if (message.getSender().getNickname().equals(service.getUser().getNickname())) {
            infoText.setText(time);
            messageBubble.setStyle("-fx-background-color: #ffffff;");
            bodyText.setStyle("-fx-text-fill: #000000;");
            bodyText.alignmentProperty().setValue(javafx.geometry.Pos.CENTER_RIGHT);
            infoText.alignmentProperty().setValue(javafx.geometry.Pos.CENTER_RIGHT);
        } else {
            infoText.setText(time + " " + message.getSender().getNickname());
            messageBubble.setStyle("-fx-background-color: #0083ff;");
            bodyText.setStyle("-fx-text-fill: #ffffff;");
            bodyText.alignmentProperty().setValue(javafx.geometry.Pos.CENTER_LEFT);
            infoText.alignmentProperty().setValue(javafx.geometry.Pos.CENTER_LEFT);
        }
        return parent;
    }
}
