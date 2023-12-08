package de.uniks.pmws2324.tiny.controller;

import de.uniks.pmws2324.tiny.App;
import de.uniks.pmws2324.tiny.service.GameService;
import javafx.scene.Parent;

public abstract class Controller {

    public App app;
    public GameService gameService;

    public Controller(App app, GameService gameService) {
        this.app = app;
        this.gameService = gameService;
    }

    /**
     * Method for loading the fxml-file and initializing any other important component
     */
    public abstract void init();

    /**
     * Method for retrieving view elements and binding values and actions to them
     *
     * @return root view element of the loaded view(element)
     */
    public abstract Parent render();

    /**
     * Method to unregister and destroy connections
     */
    public void destroy() {
        this.app = null;
        this.gameService = null;
    };
}
