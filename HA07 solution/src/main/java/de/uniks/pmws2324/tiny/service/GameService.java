package de.uniks.pmws2324.tiny.service;

import de.uniks.pmws2324.tiny.Constants;
import de.uniks.pmws2324.tiny.model.*;

import java.util.*;

public class GameService {
    private final List<Street> streets = new ArrayList<>();
    private final List<City> cities = new ArrayList<>();
    private HeadQuarter headQuarter;
    private final Random rnGenerator = new Random();

    public List<Street> getStreets() {
        return streets;
    }

    public List<City> getCities() {
        return cities;
    }


    public void initGame() {

        // HQ Kassel
        this.headQuarter = new HeadQuarter();
        this.headQuarter.setName(Constants.CITY_NAME_KASSEL).setX(Constants.CITY_X_KASSEL).setY(Constants.CITY_Y_KASSEL);

        // Paderborn
        City paderborn = new City();
        paderborn.setName(Constants.CITY_NAME_PADERBORN).setX(Constants.CITY_X_PADERBORN).setY(Constants.CITY_Y_PADERBORN);
        this.cities.add(paderborn);

        // Bad Arolsen
        City badArolsen = new City();
        badArolsen.setName(Constants.CITY_NAME_BAD_AROLSEN).setX(Constants.CITY_X_BAD_AROLSEN).setY(Constants.CITY_Y_BAD_AROLSEN);
        this.cities.add(badArolsen);

        // Marburg
        City marburg = new City();
        marburg.setName(Constants.CITY_NAME_MARBURG).setX(Constants.CITY_X_MARBURG).setY(Constants.CITY_Y_MARBURG);
        this.cities.add(marburg);

        // Eschwege
        City eschwege = new City();
        eschwege.setName(Constants.CITY_NAME_ESCHWEGE).setX(Constants.CITY_X_ESCHWEGE).setY(Constants.CITY_Y_ESCHWEGE);
        this.cities.add(eschwege);

        // Göttingen
        City goettingen = new City();
        goettingen.setName(Constants.CITY_NAME_GOETTINGEN).setX(Constants.CITY_X_GOETTINGEN).setY(Constants.CITY_Y_GOETTINGEN);
        this.cities.add(goettingen);

        // connect cities
        connectCities(this.headQuarter, eschwege);
        connectCities(this.headQuarter, marburg);
        connectCities(this.headQuarter, badArolsen);
        connectCities(this.headQuarter, goettingen);
        connectCities(badArolsen, paderborn);
        connectCities(goettingen, paderborn);
        connectCities(marburg, eschwege);

        // generate Car
        new Car().setDriver("Alice").setPosition(this.headQuarter).setOwner(this.headQuarter);

        // generate orders
        generateOrder();
        generateOrder();
    }

    private void generateOrder() {
        new Order()
                .setLocation(this.cities.get(rnGenerator.nextInt(this.cities.size())))
                .setReward(rnGenerator.nextInt(Constants.ORDER_REWARD_MIN, Constants.ORDER_REWARD_MAX))
                .setExpires(rnGenerator.nextInt(Constants.ORDER_EXPIRES_MS_MIN, Constants.ORDER_EXPIRES_MS_MAX));
    }

    public Street connectCities(City c1, City c2) {
        Street street = new Street().withConnects(c1).withConnects(c2);
        streets.add(street);
        return street;
    }

    public HeadQuarter getHeadquarter() {
        return headQuarter;
    }

    public ArrayList<Location> getPath(City start, City goal) {
        if (cities.isEmpty() || streets.isEmpty()) return null;

        Map<Location, Location> previous = new HashMap<>();
        Set<City> visited = new HashSet<>();
        Queue<Location> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Location current = queue.poll();

            if (current.equals(goal)) {
                return buildPath(start, goal, previous);
            }

            if (current instanceof City) {
                City currentCity = (City) current;
                for (Street street : streets) {
                    if (street.isBlocked() || !street.getConnects().contains(currentCity)) continue;

                    for (City nextCity : street.getConnects()) {
                        if (!visited.contains(nextCity)) {
                            visited.add(nextCity);
                            previous.put(nextCity, street); // Speichert die Straße, die zur Stadt führt
                            previous.put(street, currentCity); // Speichert die Stadt, von der aus die Straße kommt
                            queue.add(nextCity);
                        }
                    }
                }
            }
        }

        return null; // Kein Pfad gefunden
    }

    private ArrayList<Location> buildPath(Location start, Location goal, Map<Location, Location> previous) {
        LinkedList<Location> path = new LinkedList<>();
        for (Location at = goal; at != null; at = previous.get(at)) {
            path.addFirst(at);
        }
        return new ArrayList<>(path);
    }


    public void getRewardForOrder(Order order) {
        this.headQuarter.setMoney(this.headQuarter.getMoney() + order.getReward());
        order.getCar().setPosition(headQuarter);
        order.getLocation().withoutOrders(order);
        order.setCar(null);
    }

    public void setNewCarPosition(Car car, Location location) {
        car.setPosition(location);
    }
}
