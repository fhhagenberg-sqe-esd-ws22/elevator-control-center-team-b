package at.fhhagenberg.logic;

import java.util.Arrays;

import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.service.IElevatorService;

/**
 * Class responsible for controlling the elevator
 */
public class BusinesLogic {
    private final Building mModel;
    private final boolean[] mManual;
    private final int[] mManualTarget;
    //direction in which the elevator will currently try to go
    private final boolean[] mUp;
    private final boolean[] mUpTarget;
    private final boolean[] mDownTarget;

    /**
     * determines if an elevator is standing on its target floor
     * @param elevator which to check if it is standing on its target floor
     * @return true if standing on the target floor, false otherwise
     */
    private boolean isOnTargetFloor(Elevator elevator){
        return elevator.getTarget() == elevator.getNearestFloor() &&
                elevator.getDirection() == IElevatorService.ELEVATOR_DIRECTION_UNCOMMITTED &&
                elevator.getAccel() == 0 && elevator.getSpeed() == 0;
    }

    /**
     * updates the targets of the elevators that are not already set
     */
    public void setNextTargets(){

        // collect all floors where buttons are pressed in 2 arrays
        var upPressed = new boolean[mModel.getFloors().size()];
        var downPressed = new boolean[mModel.getFloors().size()];
        for(var floor : mModel.getFloors()){
            downPressed[floor.getFloorNumber()] = floor.getWantDown();
            upPressed[floor.getFloorNumber()] = floor.getWantUp();
        }

        // handle the next target for each elevator one after the other
        // once a target is set, it is fixed until the elevator arrives at that location
        for(var elevator : mModel.getElevators()){
            var nr = elevator.getElevatorNr();

            // an array for the stops, needed so we don't try to stop on our
            // current floor again, when the button is still pressed when on the floor
            var stops = new boolean[mModel.getFloors().size()];
            for(int i = 0; i < elevator.getNrOfFloors(); ++i){
                stops[i] = elevator.getStop(i);
            }

            // if the elevator is standing at its current floor
            if(isOnTargetFloor(elevator)) {

                // if we are in manual mode, we set the last entered manual target
                if (mManual[nr]) {
                    elevator.setTarget(mManualTarget[nr]);
                }
                else{ //automatic mode

                    // we don't need to stop on the floor we are currently standing on
                    var currentFloor= elevator.getNearestFloor();
                    stops[currentFloor] = false;
                    downPressed[currentFloor] = false;
                    upPressed[currentFloor] = false;

                    // check if this floor was blocked as upwards/downwards target by this elevator
                    if(mUp[nr] && mUpTarget[currentFloor]){
                        mUpTarget[currentFloor] = false;
                    }
                    if(!mUp[nr] && mDownTarget[currentFloor]){
                        mDownTarget[currentFloor] = false;
                    }

                    // if we search for stops that are on the way down, but find none we must look again for stops
                    // on the way up, for this we have to loop once, therefore this loop variable
                    var loop = 0;
                    while(loop < 2){
                        loop++;

                        // if the elevator is searching for stops upwards
                        // it starts the search one floor above the current, until the last floor is reached
                        if (mUp[nr]) {

                            int i = currentFloor + 1;
                            for (; i < elevator.getNrOfFloors(); ++i) {

                                // if a floor is a requested stop by an inside or outside button it will get serviced
                                // if requested outside, see that it is not already serviced by another elevator
                                if ((upPressed[i] && !mUpTarget[i]) || stops[i])  {
                                    if (elevator.getServiced(i)) {
                                        elevator.setTarget(i);
                                        if (upPressed[i]) {

                                            // if it was a press from an outside button, this floor is now serviced by
                                            // this elevator and other elevators will no longer see the request here
                                            mUpTarget[i] = true;
                                        }
                                        break;
                                    }
                                }
                            }

                            // if no stop upwards was found, we search downwards
                            // starting with the upmost floor
                            if (i == elevator.getNrOfFloors()) {
                                mUp[nr] = false;
                                if(currentFloor != elevator.getNrOfFloors()-1) {
                                    currentFloor = i;
                                }
                            }
                            // otherwise, we take care of the next elevator
                            else{
                                break;
                            }
                        }
                        // repeat the same process as for searching for stops upwards, just on the way down
                        if (!mUp[nr]) {

                            int i = currentFloor - 1;
                            for (; i >= 0; --i) {
                                if ((downPressed[i] && !mDownTarget[i]) || stops[i])  {
                                    if (elevator.getServiced(i)) {
                                        elevator.setTarget(i);
                                        if (downPressed[i]) {
                                            mDownTarget[i] = true;
                                        }
                                        break;
                                    }
                                }
                            }
                            if (i == -1) {
                                mUp[nr] = true;
                                if(currentFloor != 0) {
                                    currentFloor = i;
                                }
                            }
                            else{
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Constructor of BusinesLogic
     * @param building the building for which the elevators should be controled
     */
    public BusinesLogic(Building building) {
        mModel = building;
        mManual = new boolean[mModel.getElevators().size()];
        Arrays.fill(mManual, false);
        mUp = new boolean[mModel.getElevators().size()];
        Arrays.fill(mUp, true);
        mUpTarget = new boolean[mModel.getFloors().size()];
        Arrays.fill(mUpTarget, false);
        mDownTarget = new boolean[mModel.getFloors().size()];
        Arrays.fill(mDownTarget, false);
        mManualTarget = new int[mModel.getElevators().size()];
        Arrays.fill(mManualTarget, 0);
    }

    /**
     * Activates or deactivates manual mode of an elevator
     * @param elevatorNr elevator for which manual mode should be set
     * @param active if true, the elevator will be operated in manual mode
     *                  otherwise in automatic mode.
     */
    public void setManual(int elevatorNr, boolean active) {
        mManual[elevatorNr] = active;
        //set default target in manual mode to current target
        if(active){
            mManualTarget[elevatorNr] = mModel.getElevatorByNumber(elevatorNr).getTarget();
        }
    }

    /**
     * Getter for manual mode flag of an elevator
     * @param elevatorNr elevator for which the flag is retrieved
     * @return flag that indicates the manual mode
     */
    public boolean getManual(int elevatorNr) {
        return mManual[elevatorNr];
    }

    /**
     * sets the next elevator target when in manual mode
     * @param elevatorNr for which elevator to set the target
     * @param floor to which floor to set the target
     */
    public void setElevatorManualTarget(int elevatorNr, int floor) {
        // TODO: check if floor is in range??
        mManualTarget[elevatorNr] = floor;
    }
}
