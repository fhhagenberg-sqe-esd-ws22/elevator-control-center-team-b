package at.fhhagenberg.app;

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

        var scene = new Scene(initializer.getRoot(), 1200, 480);

        // TODO: set stage minimum height as nrFloors*floorHeight + minimum elevator height
        // TODO: set stage minimum width as nrElevators*eleWidth + floorWidth
        stage.setTitle("Elevator Control");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates a service that is used for the elevator control
     * @return elevator service
     */
    protected IElevatorService createService() {
        return new RMIElevatorService(null);
    }
}