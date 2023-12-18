package de.uniks.pmws2324.tiny.service;

import de.uniks.pmws2324.tiny.model.Car;
import de.uniks.pmws2324.tiny.model.City;
import de.uniks.pmws2324.tiny.model.Order;
import de.uniks.pmws2324.tiny.model.Street;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static de.uniks.pmws2324.tiny.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private City getCityByName(List<City> cities, String name) {
        return cities.stream().filter(city -> city.getName().equals(name)).findFirst().orElse(null);
    }

    private Street getStreetByCities(List<Street> streets, City city1, City city2) {
        return streets.stream().filter(street -> street.getConnects().contains(city1) && street.getConnects().contains(city2)).findFirst().orElse(null);
    }
    @Test
    void initGameTest() {
        GameService gameService = new GameService();
        gameService.initGame();
        assertEquals(5, gameService.getCities().size());
        assertEquals(7, gameService.getStreets().size());
        City kassel = gameService.getHeadquarter();
        City paderborn = getCityByName(gameService.getCities(), "Paderborn");
        City badArolsen = getCityByName(gameService.getCities(), "Bad Arolsen");
        City marburg = getCityByName(gameService.getCities(), "Marburg");
        City eschwege = getCityByName(gameService.getCities(), "Eschwege");
        City goettingen = getCityByName(gameService.getCities(), "Göttingen");

        assertNotNull(getStreetByCities(gameService.getStreets(), kassel, eschwege));
        assertNotNull(getStreetByCities(gameService.getStreets(), kassel, marburg));
        assertNotNull(getStreetByCities(gameService.getStreets(), kassel, goettingen));
        assertNotNull(getStreetByCities(gameService.getStreets(), kassel, badArolsen));
        assertNotNull(getStreetByCities(gameService.getStreets(), paderborn, badArolsen));
        assertNotNull(getStreetByCities(gameService.getStreets(), paderborn, goettingen));
        assertNotNull(getStreetByCities(gameService.getStreets(), marburg, eschwege));

        Car car = kassel.getCars().get(0);
        assertEquals("Alice", car.getDriver());

        ArrayList<Order> orders = new ArrayList<>();
        for (City city: gameService.getCities()) {
            orders.addAll(city.getOrders());
        }
        assertEquals(2, orders.size());
        for (Order order: orders) {
            assertTrue(order.getReward() >= ORDER_REWARD_MIN && order.getReward() <= ORDER_REWARD_MAX);
            assertTrue(order.getExpires() >= ORDER_EXPIRES_MS_MIN && order.getExpires() <= ORDER_EXPIRES_MS_MAX);
        }
    }
}