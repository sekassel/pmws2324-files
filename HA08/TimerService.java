package de.uniks.pmws2324.tiny.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

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
        // TODO implement
    }

    public void stop() {
        timers.forEach(Timer::cancel);
    }
}
