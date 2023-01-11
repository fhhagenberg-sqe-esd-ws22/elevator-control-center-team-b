package at.fhhagenberg.app;

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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaFX App
 */
public class ECCApp extends Application {

    @Override
    public void start(Stage stage) {
        var scene = createScene(createService());
        setupLogging();

        // TODO: set stage minimum height as nrFloors*floorHeight + minimum elevator height
        // TODO: set stage minimum width as nrElevators*eleWidth + floorWidth
        stage.setTitle("Elevator Control");
        stage.setScene(scene);
        stage.show();
    }

    private void setupLogging() {
        try {
            String loggingDirectory = "./Logs/";
            String fileName = String.format("%s.log", new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date()));
            if (!Files.exists(Paths.get(loggingDirectory))) {
                Files.createDirectory(Paths.get(loggingDirectory));
            }

            FileHandler handler;
            handler = new FileHandler(loggingDirectory + fileName);
            Logger.getGlobal().addHandler(handler);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Logger.getGlobal().severe(String.format("%s\n%s", ex.getMessage(), 
                ex.getStackTrace().toString()));
            showError(ex.getMessage());
            throw ex;
        }
        catch(UpdaterException ex) {
            Logger.getGlobal().severe(String.format("%s\n%s", ex.getMessage(), 
                ex.getStackTrace().toString()));
            showError(ex.getMessage());
            throw ex;
        }
        catch(ModelException ex) {
            Logger.getGlobal().severe(String.format("%s\n%s", ex.getMessage(), 
                ex.getStackTrace().toString()));
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