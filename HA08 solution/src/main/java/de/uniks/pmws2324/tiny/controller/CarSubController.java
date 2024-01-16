package de.uniks.pmws2324.tiny.controller;

import de.uniks.pmws2324.tiny.App;
import de.uniks.pmws2324.tiny.Main;
import de.uniks.pmws2324.tiny.model.Car;
import de.uniks.pmws2324.tiny.service.GameService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CarSubController extends Controller {
    @FXML
    Label carDriverLabel;
    @FXML
    Label carDestinationLabel;
    private Parent parent;
    private Car car;
    private Runnable onDestroy;

    public CarSubController(App app, GameService gameService, Car car) {
        super(app, gameService);
        this.car = car;
    }

    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/CarComponent.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            this.parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Parent render() {
        this.carDriverLabel.setText(car.getDriver());
        if (car.getOrder() != null) {
            this.carDestinationLabel.setText(car.getOrder().getLocation().getName());
        } else {
            this.carDestinationLabel.setText("");
        }
        return null;
    }

    public void showInto(VBox container) {
        ObservableList<Node> children = container.getChildren();
        children.add(parent);
        setOnDestroy(() -> children.remove(parent));
    }

    public void setOnDestroy(Runnable onDestroy) {
        this.onDestroy = onDestroy;
    }
}
