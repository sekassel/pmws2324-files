package de.uniks.pmws2324.nopm.controller;

import de.uniks.pmws2324.nopm.service.Service;
import de.uniks.pmws2324.nopm.App;
import javafx.scene.Parent;

public abstract class Controller {

    public App app;
    public Service service;

    public Controller(App app, Service service) {
        this.app = app;
        this.service = service;
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
        this.service = null;
    };
}
