package de.uniks.pmws2324.tiny.controller;

import de.uniks.pmws2324.tiny.App;
import de.uniks.pmws2324.tiny.model.*;
import de.uniks.pmws2324.tiny.service.GameService;
import de.uniks.pmws2324.tiny.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class GameController extends Controller {
    private static final int TRAVEL_TIME = 1;

    private HeadQuarter headQuarter;
    private Parent parent;
    private Order selectedOrder;
    @FXML
    Label hqNameLabel;
    @FXML
    Label balanceLabel;
    @FXML
    Label carDriverLabel;
    @FXML
    Label carDestinationLabel;
    @FXML
    Label orderTownLabel;
    @FXML
    Label orderTimeLabel;
    @FXML
    Label orderRewardLabel;
    @FXML
    Button orderAcceptButton;
    @FXML
    Canvas mapCanvas;

    public GameController(App app, GameService gameService) {
        super(app, gameService);
        gameService.initGame();
        this.headQuarter = gameService.getHeadQuarter();
    }

    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Game.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            this.parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Parent render() {
        // Set static display values
        // TODO set initial text for hqNameLabel, balanceLabel, orderTownLabel, orderTimeLabel, orderRewardLabel, carDestinationLabel, carDriverLabel

        // Set view listener
        // TODO orderAcceptButton should run handleAcceptOrder method

        // Register property change listeners
        // TODO property change listener to update balanceLabel
        // TODO property change listener to update map when orders of cities change
        // TODO property change listener to update labels of first car (will be changed later to display all cars)

        // Map
        // TODO clicking on map should run handleMouseClick method
        drawMap();

        return this.parent;
    }

    private void drawMap() {
        cleanCanvas();

        // TODO draw cities, streets, cars, orders...
    }

    private void cleanCanvas() {
        GraphicsContext context = mapCanvas.getGraphicsContext2D();
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
    }

    private void handleAcceptOrder(ActionEvent actionEvent) {
        // TODO implement, for details see task 3 on homework sheet
    }

    private void nextStep(List<Location> locations, Car car) {
        if (locations.size() > 0) {
            Location nextLocation = locations.get(0);

            // TODO implement setNewCarPosition method in GameService and uncomment the following line:
            // gameService.setNewCarPosition(car, nextLocation);

            locations.remove(0);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(TRAVEL_TIME), event -> nextStep(locations, car)));
            timeline.play();
        } else {
            // TODO implement finishOrder method in GameService and uncomment the following line:
            // this.gameService.finishOrder(car.getOrder());

            // TODO reset selected order, update displayed values
        }
    }

    private void handleMouseClick(double mouseX, double mouseY) {
        // TODO check if city was clicked and has an order, this will be the currently selected order
        // TODO update ui
    }

    @Override
    public void destroy() {
        super.destroy();
        this.headQuarter = null;
        // TODO remove property change listeners
    }
}
