/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

import at.fhhagenberg.model.Building;
import at.fhhagenberg.service.ElevatorServiceException;
import at.fhhagenberg.service.IElevatorService;

import java.util.ArrayList;

public class BuildingUpdater extends UpdaterBase {

    private final Building mModel;
    private final ArrayList<ElevatorUpdater> mElevatorUpdaters;
    private final ArrayList<FloorUpdater> mFloorUpdaters;

    /**
     * Constructor for the BuildingUpdater
     * @param service IElevatorService object to retrieve the necessary information for an update on a building object.
     * @param model The to be updated Building object.
     */
    public BuildingUpdater(IElevatorService service, ArrayList<ElevatorUpdater> elevatorUpdaters, ArrayList<FloorUpdater> floorUpdaters, Building model) {
        super(service);
        if (model == null || elevatorUpdaters == null || floorUpdaters == null || elevatorUpdaters.isEmpty() || floorUpdaters.isEmpty()) {
            throw new UpdaterException("Could not create BuildingUpdater, since the given Building object or the updater lists are null!");
        }

        mElevatorUpdaters = elevatorUpdaters;
        mFloorUpdaters = floorUpdaters;
        mModel = model;
    }

    /**
     * Performs all necessary API calls on a service object in order to update a referenced model object.
     */
    @Override
    public void update() {
        try {
            updateFloors();
            updateElevators();
            updateBuilding();
        }
        catch(ElevatorServiceException ex) {
            throw new UpdaterException("An error occurred during updating the building!\nError message: " + ex.getMessage());
        }
    }

    /**
     * Updates the Building object.
     */
    private void updateBuilding() {
        //TODO(PH): discuss if it is necessary to do that every update cycle
        var elevators = mModel.getElevators();
        var floors = mModel.getFloors();
        for (var e : elevators) {
            for (var f : floors) {
                boolean isServiced = mElevatorService.getServicesFloors(e.getElevatorNr(), f.getFloorNumber());
                mModel.setIsServiced(e.getElevatorNr(), f.getFloorNumber(), isServiced);
            }
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
