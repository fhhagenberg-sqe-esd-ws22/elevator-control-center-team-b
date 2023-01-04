package at.fhhagenberg.main;

import at.fhhagenberg.service.IElevatorService;
import at.fhhagenberg.service.RMIElevatorService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class ECCApp extends Application {

    @Override
    public void start(Stage stage) {
        Initializer initializer = new Initializer(createService());

        var scene = new Scene(initializer.GetRoot(), 640, 480);

        stage.setScene(scene);
        stage.show();
    }

    protected IElevatorService createService() {
        return new RMIElevatorService(null);
    }
}