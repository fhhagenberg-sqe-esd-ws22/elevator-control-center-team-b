/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.service.IElevatorService;

public class ElevatorUpdater extends UpdaterBase{

    private final Elevator mModel;

    /**
     * Constructor for the ElevatorUpdater
     * @param elevatorService IElevatorService object to retrieve the necessary information for an update on an Elevator object.
     * @param model The to be updated Elevator object.
     */
    public ElevatorUpdater(IElevatorService elevatorService, Elevator model) {
        super(elevatorService);
        if(model == null) {
            throw new UpdaterException("Could not create ElevatorUpdater because the given Elevator object is null!");
        }

        mModel = model;
    }


    /**
     * Performs all necessary API calls on a service object in order to update a referenced model object.
     */
    @Override
    public void update() {
        int elevatorNr = mModel.getElevatorNr();
        mModel.setSpeed(mElevatorService.getElevatorSpeed(elevatorNr));
        // negative accelleration in upwards speed is possible - no need for abs
        mModel.setAccel(mElevatorService.getElevatorAccel(elevatorNr));
        mModel.setTarget(mElevatorService.getTarget(elevatorNr));
        mModel.setDirection(mElevatorService.getCommittedDirection(elevatorNr));
        // no max weight specified
        mModel.setPayload(mElevatorService.getElevatorWeight(elevatorNr));
        mModel.setDoorStatus(mElevatorService.getElevatorDoorStatus(elevatorNr));
        checkForStops(elevatorNr);
        calcNearestFloor(elevatorNr);
    }

    /**
     * Calculates the nearest floor with the current position and average floor height
     * @param elevatorNr identifier for the elevator
     */
    private void calcNearestFloor(int elevatorNr)
    {
        int height = mElevatorService.getElevatorPosition(elevatorNr);
        int floorHeight = mElevatorService.getFloorHeight();

        int floor = height / floorHeight;
        if (height % floorHeight >= floorHeight / 2)
        {
            floor++;
        }

        if (floor > mModel.getNrOfFloors()) {
            floor = mModel.getNrOfFloors();
        }

        mModel.setNearestFloor(floor);
    }

    /**
     * Checks where people in the elevator want to get off
     * @param elevatorNr identifies the elevator
     */
    private void checkForStops(int elevatorNr) {
        int floors = mElevatorService.getFloorNum();
        
        for (int i = 0; i < floors; i++) {
            mModel.setStop(i, mElevatorService.getElevatorButton(elevatorNr, i));
        }
    }
}
