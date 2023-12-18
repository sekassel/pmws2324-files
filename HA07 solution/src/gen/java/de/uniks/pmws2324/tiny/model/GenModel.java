package de.uniks.pmws2324.tiny.model;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;

import java.util.List;

public class GenModel implements ClassModelDecorator {
    class Car {
        String driver;
        @Link("ownedCars")
        HeadQuarter owner;
        @Link("cars")
        Location position;
        @Link("car")
        Order order;
    }

    class HeadQuarter extends City {
        int money;
        @Link("owner")
        List<Car> ownedCars;

    }

    class Order {
        long expires;
        int reward;
        @Link("order")
        Car car;
        @Link("orders")
        City location;
    }

    class Location {
        int x;
        int y;
        @Link("position")
        List<Car> cars;
    }

    class City extends Location {
        String name;
        @Link("location")
        List<Order> orders;

        @Link("connects")
        List<Street> streets;
    }

    class Street extends Location {
        int speedLimit;
        boolean blocked;
        @Link("streets")
        List<City> connects;
    }



    @Override
    public void decorate(ClassModelManager m) {
        m.haveNestedClasses(GenModel.class);
    }
}
