/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

import at.fhhagenberg.model.Building;
import at.fhhagenberg.service.IElevatorService;

public class BuildingUpdater extends UpdaterBase {

    private final Building mModel;

    /**
     * Constructor for the BuildingUpdater
     * @param service IElevatorService object to retrieve the necessary information for an update on a building object.
     * @param model The to be updated Building object.
     */
    public BuildingUpdater(IElevatorService service, Building model) {
        super(service);
        if (model == null) {
            throw new UpdaterException("Could not create BuildingUpdater, since the given Building object is null!");
        }

        mModel = model;
    }

    /**
     * Performs all necessary API calls on a service object in order to update a referenced model object.
     */
    @Override
    public void update() {
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
}
