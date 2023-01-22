package at.fhhagenberg.app;

import at.fhhagenberg.logging.Logging;
import at.fhhagenberg.logic.AppController;
import at.fhhagenberg.logic.BusinessLogic;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.ModelException;
import at.fhhagenberg.model.ModelFactory;
import at.fhhagenberg.service.ElevatorServiceException;
import at.fhhagenberg.service.IElevatorService;
import at.fhhagenberg.service.RMIElevatorService;
import at.fhhagenberg.updater.BuildingUpdater;
import at.fhhagenberg.updater.UpdaterException;
import at.fhhagenberg.updater.UpdaterFactory;
import at.fhhagenberg.view.BuildingView;
import at.fhhagenberg.viewmodels.BuildingViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.concurrent.Executors;

/**
 * JavaFX App
 */
public class ECCApp extends Application {
    // constants for height of the window
    private static final int ELEVATOR_HEIGHT_PER_FLOOR = 40;
    private static final int ELEVATOR_HEIGHT_OFFSET = 215;

    // constants for width of the window
    private static final int ELEVATOR_WIDTH = 280;
    private static final int FLOOR_WIDTH = 185;

    private AppController mController;

    @Override
    public void start(Stage stage) {
        var service = createService();
        String shutdown = "\nThe application will now shut down.";
        if (service == null) {
            showError("The service could not be created!" + shutdown);
            return;
        }

        var scene = createScene(service);

        if (scene == null) {
            showError("Could not create the JavaFX scene object!" + shutdown);
            return;
        }

        if (mController == null) {
            showError("Something went wrongduring the apps initialization!" + shutdown);
            return;
        }

        int height = ELEVATOR_HEIGHT_OFFSET + ELEVATOR_HEIGHT_PER_FLOOR * service.getFloorNum();
        int width = ELEVATOR_WIDTH * service.getElevatorNum() + FLOOR_WIDTH;

        stage.setTitle("Elevator Control");
        stage.setHeight(height);
        stage.setWidth(width);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.onCloseRequestProperty().set((WindowEvent e) -> mController.stop());
        mController.start();
        stage.show();
    }

    /**
     * creates the scene for the GUI and instantiates all components that are needed in the background
     *
     * @return the scene of the elevator service
     */
    private Scene createScene(IElevatorService service) {
        try {
            ModelFactory factory = new ModelFactory(service);
            Building building = factory.createBuilding();
            UpdaterFactory updaterFactory = new UpdaterFactory(service);

            BuildingUpdater updater = updaterFactory.createBuildingUpdater(building);
            BusinessLogic logic = new BusinessLogic(building);
            BuildingViewModel buildingViewModel = new BuildingViewModel(building, logic);
            BuildingView buildingView = new BuildingView(buildingViewModel);

            mController = new AppController(service, updater, logic, buildingViewModel, Executors.newSingleThreadScheduledExecutor(), ECCApp::showError, ECCApp::showInfo);

            return new Scene(buildingView.getLayout(), 1200, 480);
        }
        // display the errors to the user and throw the exception again since
        // they cannot be handled in this function
        catch (ElevatorServiceException | UpdaterException | ModelException ex) {
            return null;
        }
    }

    /**
     * Displays the given content in an Alert pop up with severity error.
     *
     * @param content The message to display.
     */
    private static void showError(String content) {
        Logging.getLogger().error(content);
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Critical Error Occurred");
        alert.setContentText(content);
        alert.show();
    }

    /**
     * Displays the given content in an Alert pop up with severity information.
     *
     * @param content The message to display.
     */
    private static void showInfo(String content) {
        Logging.getLogger().info(content);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(content);
        alert.show();
    }

    /**
     * Creates a service that is used for the elevator control
     *
     * @return elevator service
     */
    protected IElevatorService createService() {
        RMIElevatorService service = new RMIElevatorService(null, "rmi://localhost/ElevatorSim");
        if (service.connect()) {
            return service;
        } else {
            return null;
        }
    }
}