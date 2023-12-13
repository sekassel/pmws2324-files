package de.uniks.pmws2324.tiny.controller;

import de.uniks.pmws2324.tiny.App;
import de.uniks.pmws2324.tiny.service.GameService;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ShopController extends Controller {
    private final GameService gameService;
    private final StackPane rootPane;
    private Pane shop;
    private Node underground;

    // TODO add variables for labels, buttons, textfield here

    public ShopController(App app, GameService gameService, StackPane rootPane) {
        super(app, gameService);
        this.gameService = gameService;
        this.rootPane = rootPane;
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

    private void handleBuyCar(MouseEvent mouseEvent) {
        // TODO get driver name from text field, call buyCar from gameService, close shop
    }

    private void handleCloseShop(MouseEvent mouseEvent) {
        // TODO remove shop
    }
}
