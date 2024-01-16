package de.uniks.pmws2324.tiny.controller;

import de.uniks.pmws2324.tiny.App;
import de.uniks.pmws2324.tiny.Main;
import de.uniks.pmws2324.tiny.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Stack;

public class ShopController extends Controller {

    //private final StackPane rootPane;
    private final GameService gameService;
    @FXML
    Label shopBalanceLabel;
    @FXML
    Label shopCarCostLabel;
    @FXML
    TextField nameInput;
    @FXML
    Button cancelButton;
    @FXML
    Button buyButton;
    @FXML
    StackPane rootPane;
    private Pane shop;
    private Node underground;

    public ShopController(App app, GameService gameService, StackPane rootPane) {
        super(app, gameService);
        this.gameService = gameService;
        this.rootPane = rootPane;
    }

    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Shop.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            shop = loader.load();
            underground = rootPane.getChildren().get(0);
            underground.setEffect(new javafx.scene.effect.BoxBlur(3, 3, 3));
            underground.setDisable(true);
            rootPane.getChildren().add(shop);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Parent render() {
        // Init the shop view
        this.shopBalanceLabel.setText(gameService.getHeadquarter().getMoney() + " €");
        this.shopCarCostLabel.setText(gameService.getHeadquarter().getNewCarPrice() + " €");
        this.cancelButton.setOnMouseClicked(this::closeShop);
        this.buyButton.setOnMouseClicked(this::buyCar);
        this.buyButton.setDisable(true);
        this.nameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 0
                    && newValue.length() < 20
                    && gameService.getHeadquarter().getMoney() >= gameService.getHeadquarter().getNewCarPrice()
            ) {
                this.buyButton.setDisable(false);
            } else {
                this.buyButton.setDisable(true);
            }
        });
        return null;
    }

    private void buyCar(MouseEvent mouseEvent) {
        String name = this.nameInput.getText();
        this.gameService.buyCar(name);
        this.closeShop(mouseEvent);
    }

    private void closeShop(MouseEvent mouseEvent) {
        this.rootPane.getChildren().remove(shop);
        underground.setEffect(null);
        underground.setDisable(false);
    }
}
