package at.fhhagenberg.app;

import at.fhhagenberg.logging.Logging;
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

import java.util.Arrays;
import java.util.Timer;

/**
 * JavaFX App
 */
public class ECCApp extends Application {

    @Override
    public void start(Stage stage) {
        var scene = createScene(createService());

        // TODO: set stage minimum height as nrFloors*floorHeight + minimum elevator height
        // TODO: set stage minimum width as nrElevators*eleWidth + floorWidth
        stage.setTitle("Elevator Control");
        stage.setScene(scene);
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
        catch(ElevatorServiceException ex) {
            Logging.getLogger().severe(String.format("%s\n%s", ex.getMessage(), 
                Arrays.toString(ex.getStackTrace())));
            showError(ex.getMessage());
            throw ex;
        }
        catch(UpdaterException ex) {
            Logging.getLogger().severe(String.format("%s\n%s", ex.getMessage(), 
                Arrays.toString(ex.getStackTrace())));
            showError(ex.getMessage());
            throw ex;
        }
        catch(ModelException ex) {
            Logging.getLogger().severe(String.format("%s\n%s", ex.getMessage(), 
                Arrays.toString(ex.getStackTrace())));
            showError(ex.getMessage());
            throw ex;
        }
    }

    private static void showError(String reason) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Critical Error Occurred");
        alert.setContentText(String.format("The app could not be started!\nReason: %s", reason));
        alert.showAndWait();
    }

    /**
     * Creates a service that is used for the elevator control
     * @return elevator service
     */
    protected IElevatorService createService() {
        return new RMIElevatorService(null);
    }
}