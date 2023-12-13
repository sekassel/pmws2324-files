package de.uniks.pmws2324.tiny.controller;

import de.uniks.pmws2324.tiny.App;
import de.uniks.pmws2324.tiny.model.Car;
import de.uniks.pmws2324.tiny.service.GameService;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class CarSubController extends Controller {
    private Parent parent;
    private final Car car;
    private final VBox container;

    // TODO add variables for labels here

    public CarSubController(App app, GameService gameService, Car car, VBox container) {
        super(app, gameService);
        this.container = container;
        this.car = car;
    }

    @Override
    public void init() {
        // TODO load view
    }

    @Override
    public Parent render() {
        // TODO initialize ui elements
        return null;
    }
}
