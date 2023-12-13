package de.uniks.pmws2324.tiny;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class AppTest extends ApplicationTest {
    private Stage stage;
    private App app;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.app = new App();
        app.start(this.stage);
    }

    @Test
    public void buyNewCar() {
        // TODO implement test
    }
}
