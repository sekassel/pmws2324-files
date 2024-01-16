package de.uniks.pmws2324.tiny.service;

import de.uniks.pmws2324.tiny.model.City;
import de.uniks.pmws2324.tiny.model.Street;

import java.util.*;

public class TimerService {
    private final Random rnGenerator = new Random();
    private final GameService gameService;

    private final List<Timer> timers = new ArrayList<>();

    public TimerService(GameService gameService) {
        this.gameService = gameService;
        changeRandomThings();
    }

    private void changeRandomThings() {
        Timer timer = new Timer();
        timers.add(timer);
        if (rnGenerator.nextBoolean()) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (gameService.getCities().stream().mapToInt(city -> city.getOrders().size()).sum() <= 5) {
                        gameService.generateOrder();
                    }
                    changeRandomThings();
                }
            }, rnGenerator.nextInt(1000, 5000));
        } else {
            // block street
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Street street = gameService.getStreets().get(rnGenerator.nextInt(gameService.getStreets().size()));
                    if (street.isBlocked()) {
                        street.setBlocked(false);
                    } else {
                        Boolean otherWay = true;
                        for (City city : gameService.getCities()) {
                            street.setBlocked(true);
                            if (gameService.getPath(city, gameService.getHeadquarter()) == null) {
                                street.setBlocked(false);
                                otherWay = false;
                                break;
                            }
                        }
                        if (otherWay) {
                            street.setBlocked(true);
                        }
                    }
                    changeRandomThings();
                }
            }, rnGenerator.nextInt(1000, 10000));
        }
    }

    public void stop() {
        timers.forEach(Timer::cancel);
    }
}
