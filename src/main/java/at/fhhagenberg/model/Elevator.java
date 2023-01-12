package at.fhhagenberg.model;

import at.fhhagenberg.logging.Logging;
import at.fhhagenberg.service.IElevatorService;

import java.util.Arrays;
import java.util.logging.Level;

// contains all information of a single elevator
public class Elevator {
    // number of this elevator
    private final int mElevatorNr;

    private final int mNrOfFloors;
    // container with all floors where people want to get off the elevator
    private final boolean[] mStops;
    // speed of the elevator
    private int mSpeed;
    // current acceleration of the elevator
    private int mAccel;
    // the next planned elevator stops
    private int mTarget;
    // current direction of the elevator
    private int mDirection;
    // current weight inside the elevator - persons and luggage
    private int mPayload;
    // status of the doors - open/closing/closed
    private int mDoorStatus;
    // nearest floor of this elevator
    private int mNearestFloor;
    //which floors are serviced and not serviced by this elevator
    private final boolean[] mServiced;

    private static final String CLASS_NAME = "Elevator";

    /**
     * Constructor of an elevator
     * @param elevatorNr number for identifying this elevator
     * @param numberOfFloors the number ofa all floors the elevator can target
     */
    public Elevator(int elevatorNr, int numberOfFloors)
    {
        mNrOfFloors = numberOfFloors;
        mElevatorNr = elevatorNr;
        mStops = new boolean[numberOfFloors];
        mServiced = new boolean[numberOfFloors];
        Arrays.fill(mServiced, true);
        mSpeed = 0;
        mAccel = 0;
        mTarget = 0;
        mPayload = 0;
        mDirection = IElevatorService.ELEVATOR_DIRECTION_UNCOMMITTED;
        mDoorStatus = IElevatorService.ELEVATOR_DOORS_CLOSED;
        mNearestFloor = 0;

    }

    /**
     * Setter for the elevators speed.
     * @param speed The current speed of the elevator.
     */
    public void setSpeed(int speed) {
        // we have a direction - no negative speeds needed
        this.mSpeed = Math.abs(speed);
    }

    /**
     * Setter for the elevators current acceleration.
     * @param accel The current acceleration of the elevator.
     */
    public void setAccel(int accel) {
        this.mAccel = accel;
    }

    /**
     * Setter for the elevators current target floor
     * @param target The current target floor of the elevator.
     */
    public void setTarget(int target) {
        if (target >= mNrOfFloors || target < 0) {
            Logging.getLogger().logp(Level.WARNING, CLASS_NAME, "setTarget",
                String.format("Given target floor %d is out of the valid range [%d - %d[ and will not be set!",
                        target, 0, mNrOfFloors));
            return;
        }
        this.mTarget = target;
    }

    /**
     * Setter for the current direction of the elevator.
     * @param direction The current dircetion of the elevator.
     */
    public void setDirection(int direction) {
        if (direction < IElevatorService.ELEVATOR_DIRECTION_UP || direction > IElevatorService.ELEVATOR_DIRECTION_UNCOMMITTED) {
            Logging.getLogger().logp(Level.WARNING, CLASS_NAME, "setDirection",
                String.format("Given direction %d is out of the valid range [%d - %d] and will not be set!",
                        direction, IElevatorService.ELEVATOR_DIRECTION_UP, IElevatorService.ELEVATOR_DIRECTION_UNCOMMITTED));
            return;
        }
        this.mDirection = direction;
    }

    /**
     * Setter for the elevator's current payload (weight of all passenger's currently inside the elevator).
     * @param payload Current payload.
     */
    public void setPayload(int payload) {
        if (payload < 0) {
            Logging.getLogger().logp(Level.WARNING, CLASS_NAME, "setPayload",
            String.format("Negative payload is invalid - payload %d will not be set!", payload));
            return;
        }
        this.mPayload = payload;
    }

    /**
     * Setter for the current door status of the elevator.
     * @param doorStatus Current status.
     */
    public void setDoorStatus(int doorStatus) {
        if (doorStatus < IElevatorService.ELEVATOR_DOORS_OPEN || doorStatus > IElevatorService.ELEVATOR_DOORS_CLOSING) {
            Logging.getLogger().logp(Level.WARNING, CLASS_NAME, "setDoorStatus", 
                String.format("Given door status %d is out of the valid range [%d - %d] and will not be set!!", 
                    doorStatus, IElevatorService.ELEVATOR_DOORS_OPEN, IElevatorService.ELEVATOR_DOORS_CLOSING));
            return;
        }

        this.mDoorStatus = doorStatus;
    }

    /**
     * Setter for the currently nearest floor.
     * @param nearestFloor Currently nearest floor.
     */
    public void setNearestFloor(int nearestFloor) {
        if (nearestFloor < 0 || nearestFloor >= mNrOfFloors) {
            Logging.getLogger().logp(Level.WARNING, CLASS_NAME, "setNearestFloor",
                String.format("Given floor %d is out of the valid range [%d - %d[ and will not be set!",
                            nearestFloor, 0, mNrOfFloors));
            return;
        }
        this.mNearestFloor = nearestFloor;
    }

    /**
     * Sets a flag if the elevator should stop at a certain floor (request from inside the elevator)
     * @param floorNr identifies at which floor the flag should be set
     * @param doStop true if the elevator should stop at that floor, false otherwise
     */
    public void setStop(int floorNr, boolean doStop) {
        if (floorNr < 0 || floorNr >= mNrOfFloors) {
            Logging.getLogger().logp(Level.WARNING, CLASS_NAME, "setStop",
                String.format("Given floor %d is out of the valid range [%d - %d[ and will not be set!",
                            floorNr, 0, mNrOfFloors));
            return;
        }
        mStops[floorNr] = doStop;
    }

    /**
     * Sets a flag if the elevator is servicing a certain floor
     * @param floorNr identifies at which floor the flag should be set
     * @param isServiced true if the elevator services that floor, false otherwise
     */
    public void setServiced(int floorNr, boolean isServiced) {
        if (floorNr < 0 || floorNr >= mNrOfFloors) {
            Logging.getLogger().logp(Level.WARNING, CLASS_NAME, "setServiced",
                String.format("Given floor %d is out of the valid range %d - %d and will not be set!",
                                floorNr, 0, mNrOfFloors));
            return;
        }
        mServiced[floorNr] = isServiced;
    }

    /**
     * Getter for the elevator's number (is unique).
     * @return The elevator's number
     */
    public int getElevatorNr() {
        return mElevatorNr;
    }

    /**
     * Getter for the number of floors which can be targeted by the elevator.
     * @return The number of floors which can be targeted by the elevator.
     */
    public int getNrOfFloors() {
        return mNrOfFloors;
    }

    /**
     * Getter for current speed of the elevator
     * @return current speed
     */
    public int getSpeed() {
        return mSpeed;
    }

    /**
     * Getter for current accelleration of the elevator
     * @return current acceleration
     */
    public int getAccel() {
        return mAccel;
    }

    /**
     * Getter for the current target floor of the elevator
     * @return next planned stop of the elevator
     */
    public int getTarget() {
        return mTarget;
    }

    /**
     * Getter for the current direction of the elevator
     * @return current direction of the elevator
     */
    public int getDirection() {
        return mDirection;
    }

    /**
     * Getter for the current payload of the elevator
     * @return current payload of the elevator
     */
    public int getPayload() {
        return mPayload;
    }

    /**
     * Getter for the current status of the elevator door
     * @return current status of the elevator door
     */
    public int getDoorStatus() {
        return mDoorStatus;
    }

    /**
     * Getter for the nearest floor of the elevator
     * @return nearest floor of the elevator
     */
    public int getNearestFloor() {
        return mNearestFloor;
    }

    /**
     * Getter for the flag if the elevator should stop at a floor (button inside elevator)
     * @param floorNr identifies the floor
     * @return true if the elevator should stop at that floor, false if not or the floor is invalid
     */
    public boolean getStop(int floorNr) throws ModelException {
        if (floorNr < 0 || floorNr >= mNrOfFloors) {
            throw new ModelException(String.format("Floor %d is invalid!%nFloor must be >= 0 and < amount of floors (%d)", floorNr, mNrOfFloors));
        }
        return mStops[floorNr];
    }

    /**
     * Getter for the flag if the elevator is servicing a certain floor
     * @param floorNr identifies the floor
     * @return true if the elevator services that floor, false otherwise
     */
    public boolean getServiced(int floorNr) throws ModelException {
        if (floorNr < 0 || floorNr >= mNrOfFloors) {
            throw new ModelException(String.format("Floor %d is invalid!%nFloor must be >= 0 and < amount of floors (%d)", floorNr, mNrOfFloors));
        }
        return mServiced[floorNr];
    }
}
