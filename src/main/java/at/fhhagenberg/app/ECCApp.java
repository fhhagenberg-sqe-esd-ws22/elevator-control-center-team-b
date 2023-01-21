package at.fhhagenberg.app;

import at.fhhagenberg.logging.Logging;
import at.fhhagenberg.logic.BusinessLogic;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.ModelException;
import at.fhhagenberg.model.ModelFactory;
import at.fhhagenberg.service.ElevatorServiceException;
import sqelevator.IElevator;
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

import java.rmi.Naming;
import java.util.Timer;

/**
 * JavaFX App
 */
public class ECCApp extends Application {
    // constants for height of the window
    private static final int ELEVATOR_HEIGHT_PER_FLOOR = 40;
    private static final int ELEVATOR_HEIGHT_OFFSET = 215;

    // constants for width of the window
    private static final int ELEVATOR_WIDTH = 250;
    private static final int FLOOR_WIDTH = 185;

    @Override
    public void start(Stage stage) {
        var service = createService();
        
        if (service == null) {
            showError("The service could not be created!\nThe application will now shut down");
            return;
        }
        
        var scene = createScene(service);

        if (scene == null) {
            showError("The app could not be started!\nThe application will now shut down");
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
        stage.show();
    }

    /**
     * creates the scene for the GUI and instantiates all components that are needed in the background
     * @return the scene of the elevator service
     */
    private Scene createScene(IElevatorService service) {
        try {
            ModelFactory factory = new ModelFactory(service);
            Building building = factory.createBuilding();
            UpdaterFactory updaterFactory = new UpdaterFactory(service);

            BuildingUpdater updater = updaterFactory.createBuildingUpdater(building);
            BusinessLogic logic = new BusinessLogic(building);
            BuildingViewModel buildingViewModel = new BuildingViewModel(updater, building, logic, new Timer());
            BuildingView buildingView = new BuildingView(buildingViewModel);

            return new Scene(buildingView.getLayout(), 1200, 480);
        }
        // display the errors to the user and throw the exception again since
        // they cannot be handled in this function
        catch(ElevatorServiceException | UpdaterException | ModelException ex) {
            return null;
        }
    }

    private static void showError(String context) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Critical Error Occurred");
        alert.setContentText(context);
        alert.show();
    }

    /**
     * Creates a service that is used for the elevator control
     * @return elevator service
     */
    protected IElevatorService createService() {
        IElevator controller = null;
        try {
            controller = (IElevator) Naming.lookup("rmi://localhost/ElevatorSim");
        }
        catch (Exception e){
            // if the service is not available, there is no saving the program
            Logging.getLogger().error(String.format("Failed to create the service!%n%s", 
                e.getMessage()));
            return null;
        }

        return new RMIElevatorService(controller);
    }
}