/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

import at.fhhagenberg.logging.Logging;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.service.ElevatorServiceException;
import at.fhhagenberg.service.IElevatorService;
import javafx.scene.control.Alert;

import java.util.List;

public class BuildingUpdater extends UpdaterBase {

    private final List<ElevatorUpdater> mElevatorUpdaters;
    private final List<FloorUpdater> mFloorUpdaters;

    private boolean mShowedError;

    private int mFailureCnt;
    private static final int DISPLAY_MESSAGE_FAILURE_CNT = 100;

    /**
     * Constructor for the BuildingUpdater
     * @param service IElevatorService object to retrieve the necessary information for an update on a building object.
     * @param model The to be updated Building object.
     */
    public BuildingUpdater(IElevatorService service, List<ElevatorUpdater> elevatorUpdaters, List<FloorUpdater> floorUpdaters, Building model) {
        super(service);
        if (model == null || elevatorUpdaters == null || floorUpdaters == null || elevatorUpdaters.isEmpty() || floorUpdaters.isEmpty()) {
            throw new UpdaterException("Could not create BuildingUpdater, since the given Building object or the updater lists are null!");
        }

        mElevatorUpdaters = elevatorUpdaters;
        mFloorUpdaters = floorUpdaters;
        mShowedError = false;
        mFailureCnt = 0;
    }

    /**
     * Performs all necessary API calls on a service object in order to update a referenced model object.
     */
    @Override
    public void update() throws ElevatorServiceException {
        try {
            updateFloors();
            updateElevators();
            if (mShowedError) {
                resetErrorState();
            }
        }
        catch (ElevatorServiceException ex) {
            handleUpdateError(ex.getMessage());
        }
    }

    private void resetErrorState() {
        mFailureCnt = 0;
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Connection re-established");
        info.setContentText("The connection was re-established");
        mShowedError = false;
    }

    private void handleUpdateError(String message) {
        mFailureCnt++;
        if (!mShowedError) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Critical Error Occurred");
            alert.setContentText(message);
            alert.show();
            Logging.getLogger().error(message);
            mShowedError = true;
        }
        if (mFailureCnt == DISPLAY_MESSAGE_FAILURE_CNT) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Critical Error Occurred");
            alert.setContentText("The last " + mFailureCnt + " update cycles have failed! Please check your internet connection and maybe restart the application.");
            alert.show();
            Logging.getLogger().error(message);
        }
    }

    /**
     * Triggers all FloorUpdaters
     */
    private void updateFloors() {
        for(var updater : mFloorUpdaters) {
            updater.update();
        }
    }

    /**
     * Triggers all ElevatorUpdaters
     */
    private void updateElevators() {
        for(var updater : mElevatorUpdaters) {
            updater.update();
        }
    }
}
