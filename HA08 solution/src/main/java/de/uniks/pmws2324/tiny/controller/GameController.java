package de.uniks.pmws2324.tiny.controller;

import de.uniks.pmws2324.tiny.App;
import de.uniks.pmws2324.tiny.model.*;
import de.uniks.pmws2324.tiny.service.GameService;
import de.uniks.pmws2324.tiny.Main;
import de.uniks.pmws2324.tiny.service.TimerService;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.uniks.pmws2324.tiny.Constants.FIELD_DIM;

public class GameController extends Controller {
    private static final int TRAVEL_TIME = 1;
    private final TimerService timerService;
    private ShopController shopController;
    private HeadQuarter headQuarter;
    private Parent parent;
    private Order selectedOrder;
    @FXML
    Label hqNameLabel;
    @FXML
    Label balanceLabel;
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
    @FXML
    StackPane rootPane;
    @FXML
    Button shopButton;
    @FXML
    Label carCostLabel;
    @FXML
    VBox carBox;

    public GameController(App app, GameService gameService) {
        super(app, gameService);
        gameService.initGame();
        this.headQuarter = gameService.getHeadquarter();
        timerService = new TimerService(gameService);
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
        carCostLabel.setText(headQuarter.getNewCarPrice() + "€");

        // Set view listener
        orderAcceptButton.setOnAction(this::handleAcceptOrder);
        shopController = new ShopController(app, gameService, rootPane);
        shopButton.setOnAction(event -> {
            shopController.init();
            shopController.render();
        });

        // Register property change listener
        this.headQuarter.listeners().addPropertyChangeListener(HeadQuarter.PROPERTY_MONEY, evt -> {
            balanceLabel.textProperty().setValue(evt.getNewValue() + " €");
        });
        this.headQuarter.getOwnedCars().get(0).listeners().addPropertyChangeListener(Car.PROPERTY_POSITION, evt -> {
            drawMap();
        });
        this.headQuarter.listeners().addPropertyChangeListener(HeadQuarter.PROPERTY_NEW_CAR_PRICE, evt -> {
            this.carCostLabel.setText(this.headQuarter.getNewCarPrice() + "€");
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
        this.gameService.getHeadquarter().listeners().addPropertyChangeListener(HeadQuarter.PROPERTY_CARS, this::displayCar);
        displayCar(null);

        mapCanvas.setOnMouseClicked(event -> handleMouseClick(event.getX(), event.getY()));
        // draw on canvas
        drawMap();

        return this.parent;
    }

    private void displayCar(PropertyChangeEvent propertyChangeEvent) {
        this.carBox.getChildren().clear();
        for (Car car : this.gameService.getCars()) {
            CarSubController carSubController = new CarSubController(app, gameService, car);
            carSubController.init();
            carSubController.render();
            carSubController.showInto(carBox);
        }
    }

    private void drawMap() {
        cleanCanvas();
        GraphicsContext context = mapCanvas.getGraphicsContext2D();

        // Draw Streets
        City cityOne;
        City cityTwo;
        for (Street street : this.gameService.getStreets()) {
            context.setStroke(Color.BLACK);
            context.setLineWidth(5);
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
            if (street.isBlocked()) {
                int x = (street.getConnects().get(0).getX() + FIELD_DIM / 2) + ((street.getConnects().get(1).getX() - street.getConnects().get(0).getX()) / 2);
                int y = (street.getConnects().get(0).getY() + FIELD_DIM / 2) + ((street.getConnects().get(1).getY() - street.getConnects().get(0).getY()) / 2);
                context.setStroke(Color.RED);
                context.strokeLine(x - 5, y - 5, x + 5, y + 5);
                context.strokeLine(x + 5, y - 5, x - 5, y + 5);
            }
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
        int x;
        int y;
        for (Location location : gameService.getLocations()) {
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
                context.fillOval(city.getX() + FIELD_DIM / 2, city.getY() + FIELD_DIM / 2, 15, 15);
                context.setFill(Color.WHITE);
                context.fillText(Integer.toString(city.getOrders().size()), city.getX() + FIELD_DIM / 2 + 4, city.getY() + 12 + FIELD_DIM / 2);
            }
        }
    }

    private void setOrder(Order selectedOrder) {
        this.selectedOrder = selectedOrder;
        if (selectedOrder != null) {
            this.orderAcceptButton.setDisable(this.gameService.getAvailableCar() != null);
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
        gameService.getAvailableCar().setOrder(selectedOrder);
        startOrder(selectedOrder);
        setOrder(null);
    }

    private void startOrder(Order selectedOrder) {
        System.out.println("Start order with " + selectedOrder.getCar().getDriver() + " to " + selectedOrder.getLocation().getName() + " for " + selectedOrder.getReward() + " €");
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
            System.out.println(car.getDriver() + " arrived at destination ");
            if (car.getOrder() == null) {
                System.out.println("Order is null");
                return;
            }
            this.gameService.getRewardForOrder(car.getOrder());
        }
    }

    private void handleMouseClick(double mouseX, double mouseY) {
        City selectedCity = gameService.getCities().stream()
                .filter(city -> city.getX() < mouseX
                        && city.getX() + FIELD_DIM > mouseX
                        && city.getY() < mouseY
                        && city.getY() + FIELD_DIM > mouseY)
                .findFirst().orElse(null);
        if (selectedCity != null) {
            Order possibleOrder = selectedCity.getOrders().stream()
                    .filter(order -> order.getLocation() == selectedCity && order.getCar() == null)
                    .findFirst().orElse(null);
            if (possibleOrder != null) {
                setOrder(possibleOrder);
                orderAcceptButton.setDisable(this.gameService.getAvailableCar() == null);
            } else {
                setOrder(null);
                orderAcceptButton.setDisable(true);
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("Destroying GameController");
        timerService.stop();
        this.headQuarter.listeners().removePropertyChangeListener(HeadQuarter.PROPERTY_MONEY, evt -> {
            balanceLabel.textProperty().setValue(evt.getNewValue() + " €");
        });
        this.headQuarter.getOwnedCars().get(0).listeners().removePropertyChangeListener(Car.PROPERTY_POSITION, evt -> {
            drawMap();
        });
        for (City city : gameService.getCities()) {
            city.listeners().removePropertyChangeListener(City.PROPERTY_ORDERS, evt -> {
                drawMap();
            });
        }
        for (Car car : this.headQuarter.getCars()) {
            car.listeners().removePropertyChangeListener(Car.PROPERTY_POSITION, evt -> drawMap());
            car.listeners().removePropertyChangeListener(Car.PROPERTY_ORDER, this::displayCar);
        }
        this.gameService.getHeadquarter().getCars().get(0).listeners().removePropertyChangeListener(Car.PROPERTY_ORDER, this::displayCar);
        this.headQuarter.listeners().removePropertyChangeListener(HeadQuarter.PROPERTY_NEW_CAR_PRICE, evt -> {
            this.carCostLabel.setText(this.headQuarter.getNewCarPrice() + "€");
        });
        this.headQuarter = null;
        super.destroy();
    }
}
