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

        var target = mElevatorService.getTarget(elevatorNr);
        var direction = mElevatorService.getCommittedDirection(elevatorNr);
        if(direction != mModel.getDirection()){
            mElevatorService.setCommittedDirection(elevatorNr, mModel.getDirection());

            // model gets updated with gotten direction, new direction
            // will take one update cycle to propagate
            mModel.setDirection(direction);
        }

        if(target != mModel.getTarget()){
            mElevatorService.setTarget(elevatorNr, mModel.getTarget());
            mModel.setTarget(target);
        }

        mModel.setSpeed(mElevatorService.getElevatorSpeed(elevatorNr));
        // negative acceleration in upwards speed is possible - no need for abs
        mModel.setAccel(mElevatorService.getElevatorAccel(elevatorNr));
        // no max weight specified
        mModel.setPayload(mElevatorService.getElevatorWeight(elevatorNr));
        mModel.setDoorStatus(mElevatorService.getElevatorDoorStatus(elevatorNr));
        checkForStops(elevatorNr);
        checkServiced(elevatorNr);
        mModel.setNearestFloor(mElevatorService.getElevatorFloor(elevatorNr));
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

    /**
     * Checks which floors are serviced
     * @param elevatorNr identifies the elevator
     */
    private void checkServiced(int elevatorNr) {
        int floors = mElevatorService.getFloorNum();

        for (int i = 0; i < floors; i++) {
            mModel.setServiced(i, mElevatorService.getServicesFloors(elevatorNr, i));
        }
    }
}
