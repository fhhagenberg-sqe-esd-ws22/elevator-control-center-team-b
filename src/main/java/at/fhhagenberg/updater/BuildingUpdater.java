/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

import at.fhhagenberg.logging.Logging;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.service.ElevatorServiceException;
import at.fhhagenberg.service.IElevatorService;

import java.util.List;

public class BuildingUpdater extends UpdaterBase {

    private final List<ElevatorUpdater> mElevatorUpdaters;
    private final List<FloorUpdater> mFloorUpdaters;

    private int mFailureCnt;
    private static final int MAX_FAILURE_CNT = 10;

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
        mFailureCnt = 0;
    }

    /**
     * Function to retrieve the number of failed update cycles. The counter will be reset to 0
     * after one successful update cycle.
     * @return Number of failed update cycles.
     */
    public int getFailureCnt() { return mFailureCnt; }

    /**
     * Performs all necessary API calls on a service object in order to update a referenced model object.
     */
    @Override
    public void update() {
        try {
            updateFloors();
            updateElevators();

            mFailureCnt = 0;
        }
        catch (ElevatorServiceException ex) {
            handleUpdateError(ex.getMessage());
        }
    }

    private void handleUpdateError(String message) {
        Logging.getLogger().error(message);
        mFailureCnt++;
        if (mFailureCnt >= MAX_FAILURE_CNT) {
            throw new UpdaterException("The last " + mFailureCnt + " update cycles have failed! Please check your internet connection and restart the application.");
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
