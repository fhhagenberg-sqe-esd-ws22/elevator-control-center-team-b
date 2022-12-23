/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.service.IElevator;
import at.fhhagenberg.service.IElevatorService;

import java.rmi.RemoteException;

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
        //TODO(PH): in the update function no exception handling happens, because if one exception occurres in the midlle of updating
        //than the whole state of the elevator may be invalid. Therefor the exception handling may be handled one layer above (maybe some retries and if it doens't work
        //display that in the GUI and tell the user to check the connection to the elevator service

        int elevatorNr = mModel.getElevatorNr();
        int speed = mElevatorService.getElevatorSpeed(elevatorNr);
        // we have a direction - no negative speeds needed
        if (speed < 0) {
            speed *= -1;
        }
        mModel.setSpeed(speed);

        // negative accelleration in upwards speed is possible - no need for abs
        mModel.setAccel(mElevatorService.getElevatorAccel(elevatorNr));
        mModel.setTarget(mElevatorService.getTarget(elevatorNr));
        mModel.setDirection(mElevatorService.getCommittedDirection(elevatorNr));
        // no max weight specified
        mModel.setPayload(mElevatorService.getElevatorWeight(elevatorNr));
        mModel.setDoorStatus(mElevatorService.getElevatorDoorStatus(elevatorNr));

        calcNearestFloor(elevatorNr);
    }

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
}