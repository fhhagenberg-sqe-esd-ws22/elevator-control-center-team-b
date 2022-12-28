/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

import at.fhhagenberg.model.Floor;
import at.fhhagenberg.service.IElevatorService;

public class FloorUpdater extends UpdaterBase{
    private final Floor mModel;

    /**
     * Constructor for the FloorUpdater
     * @param service IElevatorService object to retrieve the necessary information for an update on a Floor object.
     * @param model The to be updated Floor object.
     */
    public FloorUpdater(IElevatorService service, Floor model) {
        super(service);
        if (model == null) {
            throw new UpdaterException("Could not create FloorUpdater because the given Floor object is null!");
        }

        mModel = model;
    }

    /**
     * Performs all necessary API calls on a service object in order to update a referenced model object.
     */
    @Override
    public void update() {
        int floorNr = mModel.getFloorNumber();
        mModel.setWantUp(mElevatorService.getFloorButtonUp(floorNr));
        mModel.setWantDown(mElevatorService.getFloorButtonDown(floorNr));
    }
}
