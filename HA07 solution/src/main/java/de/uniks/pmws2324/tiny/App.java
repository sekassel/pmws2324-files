package de.uniks.pmws2324.tiny;

import de.uniks.pmws2324.tiny.controller.Controller;
import de.uniks.pmws2324.tiny.controller.GameController;
import de.uniks.pmws2324.tiny.service.GameService;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class App extends Application {
    private Stage stage;
    private Controller gameController;
    private GameService gameService;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        gameService = new GameService();
        gameController = new GameController(this, gameService);

        gameController.init();
        Scene scene = new Scene(gameController.render());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        CSSFX.start();

        stage.setTitle("Tiny Transport");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        cleanup();
    }

    private void cleanup() {
        if (gameController != null) {
            gameController.destroy();
            gameController = null;
        }
    }
}
