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

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.uniks.pmws2324.tiny.Constants.FIELD_DIM;

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
        this.headQuarter = gameService.getHeadquarter();
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
        hqNameLabel.setText(headQuarter.getName());
        balanceLabel.setText(headQuarter.getMoney() + " €");
        orderRewardLabel.setText("");
        orderTimeLabel.setText("");
        orderTownLabel.setText("");

        // Display the first car (will be changed later to display all cars)
        displayCar(null);
        this.gameService.getHeadquarter().getCars().get(0).listeners().addPropertyChangeListener(Car.PROPERTY_ORDER, this::displayCar);

        // Set view listener
        orderAcceptButton.setOnAction(this::handleAcceptOrder);

        // Register property change listener
        this.headQuarter.listeners().addPropertyChangeListener(HeadQuarter.PROPERTY_MONEY, evt -> {
            balanceLabel.textProperty().setValue(evt.getNewValue() + " €");
        });
        this.headQuarter.getOwnedCars().get(0).listeners().addPropertyChangeListener(Car.PROPERTY_POSITION, evt -> {
            drawMap();
        });
        for (City city : gameService.getCities()) {
            city.listeners().addPropertyChangeListener(City.PROPERTY_ORDERS, evt -> {
                drawMap();
            });
        }

        for (Car car : this.headQuarter.getCars()) {
            car.listeners().addPropertyChangeListener(Car.PROPERTY_POSITION, evt -> drawMap());
            car.listeners().addPropertyChangeListener(Car.PROPERTY_ORDER, this::displayCar);
        }

        mapCanvas.setOnMouseClicked(event -> handleMouseClick(event.getX(), event.getY()));
        // draw on canvas
        drawMap();

        return this.parent;
    }

    private void displayCar(PropertyChangeEvent propertyChangeEvent) {
        // Todo: Change to list later
        if (this.headQuarter.getCars().size() > 0) {
            Car car = this.headQuarter.getCars().get(0);
            this.carDriverLabel.setText(car.getDriver());
            if (car.getOrder() != null) {
                this.carDestinationLabel.setText(car.getOrder().getLocation().getName());
            } else {
                this.carDestinationLabel.setText("");
            }
        }
    }

    private void drawMap() {
        cleanCanvas();
        GraphicsContext context = mapCanvas.getGraphicsContext2D();

        // Draw Streets
        City cityOne;
        City cityTwo;
        context.setStroke(Color.BLACK);
        context.setLineWidth(5);
        for (Street street : this.gameService.getStreets()) {
            cityOne = street.getConnects().get(0);
            cityTwo = street.getConnects().get(1);
            context.strokeLine(
                    cityOne.getX() + FIELD_DIM / 2,
                    cityOne.getY() + FIELD_DIM / 2,
                    cityTwo.getX() + FIELD_DIM / 2,
                    cityTwo.getY() + FIELD_DIM / 2
            );
            int length = (int) Math.sqrt(Math.pow(cityOne.getX() - cityTwo.getX(), 2) + Math.pow(cityOne.getY() - cityTwo.getY(), 2));
            context.setFill(Color.GREY);
            context.fillText(
                    Integer.toString(length),
                    ((cityOne.getX() + cityTwo.getX()) / 2) + (FIELD_DIM / 2 - 10),
                    (cityOne.getY() + cityTwo.getY()) / 2
            );
        }

        // Draw HQ with red border
        context.setStroke(Color.RED);
        context.setLineWidth(5);
        context.setFill(Color.YELLOW);
        context.fillRect(this.gameService.getHeadquarter().getX(), this.gameService.getHeadquarter().getY(), FIELD_DIM, FIELD_DIM);
        context.strokeRect(this.gameService.getHeadquarter().getX(), this.gameService.getHeadquarter().getY(), FIELD_DIM, FIELD_DIM);
        context.setFill(Color.BLACK);
        context.fillText(this.gameService.getHeadquarter().getName(), this.gameService.getHeadquarter().getX(), this.gameService.getHeadquarter().getY() - 5);

        // Draw Cities
        for (City city : this.gameService.getCities()) {
            context.setFill(Color.YELLOW);
            context.fillRect(city.getX(), city.getY(), FIELD_DIM, FIELD_DIM);
            context.setFill(Color.BLACK);
            context.fillText(city.getName(), city.getX(), city.getY());
        }


        // Draw Cars
        List<Location> locationList = new ArrayList<>();
        locationList.addAll(this.gameService.getCities());
        locationList.addAll(this.gameService.getStreets());
        locationList.add(this.gameService.getHeadquarter());
        int x;
        int y;
        for (Location location : locationList) {
            for (Car car : location.getCars()) {
                x = car.getPosition().getX() + 10;
                y = car.getPosition().getY() + 10;
                if (location instanceof Street) {
                    Street street = (Street) location;
                    x = (street.getConnects().get(0).getX() + FIELD_DIM / 2) + ((street.getConnects().get(1).getX() - street.getConnects().get(0).getX()) / 2);
                    y = (street.getConnects().get(0).getY() + FIELD_DIM / 2) + ((street.getConnects().get(1).getY() - street.getConnects().get(0).getY()) / 2);
                }
                context.setFill(Color.RED);
                context.fillOval(x, y, FIELD_DIM / 4, FIELD_DIM / 4);
            }
        }


        // Draw Orders
        for (City city : this.gameService.getCities()) {
            if (city.getOrders().size() > 0) {
                context.setFill(Color.BLUE);
                context.fillOval(city.getX() + FIELD_DIM / 2, city.getY() + FIELD_DIM / 2, 10, 10);
            }
        }
    }

    private void setOrder(Order selectedOrder) {
        this.selectedOrder = selectedOrder;
        if (selectedOrder != null) {
            this.orderAcceptButton.setDisable(this.gameService.getHeadquarter().getCars().size() == 0);
            this.orderRewardLabel.setText(selectedOrder.getReward() + " €");
            this.orderTimeLabel.setText(((int) (selectedOrder.getExpires() / 1000 / 60)) + ":" + ((int) ((selectedOrder.getExpires() / 1000) % 60)));
            this.orderTownLabel.setText(selectedOrder.getLocation().getName());
        } else {
            this.orderAcceptButton.setDisable(true);
            this.orderRewardLabel.setText("");
            this.orderTimeLabel.setText("");
            this.orderTownLabel.setText("");
        }
    }

    private void cleanCanvas() {
        GraphicsContext context = mapCanvas.getGraphicsContext2D();
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
    }

    private void handleAcceptOrder(ActionEvent actionEvent) {
        this.gameService.getHeadquarter().getCars().get(0).setOrder(selectedOrder);
        startOrder(selectedOrder);
    }

    private void startOrder(Order selectedOrder) {
        City end = selectedOrder.getLocation();
        ArrayList<Location> path = gameService.getPath(this.gameService.getHeadquarter(), end);
        if (path != null) {
            nextStep(path, selectedOrder.getCar());
        }
    }

    private void nextStep(List<Location> locations, Car car) {
        if (locations.size() > 0) {
            Location nextLocation = locations.get(0);
            gameService.setNewCarPosition(car, nextLocation);
            locations.remove(0);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(TRAVEL_TIME), event -> nextStep(locations, car)));
            timeline.play();
        } else {
            this.gameService.getRewardForOrder(car.getOrder());
            setOrder(null);
        }
    }

    private void handleMouseClick(double mouseX, double mouseY) {
        City selectedCity = gameService.getCities().stream()
                .filter(city -> city.getX() < mouseX && city.getX() + FIELD_DIM > mouseX && city.getY() < mouseY && city.getY() + FIELD_DIM > mouseY).findFirst().orElse(null);
        if (selectedCity != null && selectedCity.getOrders().size() > 0) {
            setOrder(selectedCity.getOrders().get(0));
            orderAcceptButton.setDisable(this.gameService.getHeadquarter().getCars().size() == 0);
        } else {
            setOrder(null);
            orderAcceptButton.setDisable(true);
        }
    }

    @Override
    public void destroy() {
        this.headQuarter = null;
        //TODO: Remove all listeners
        super.destroy();
    }
}
