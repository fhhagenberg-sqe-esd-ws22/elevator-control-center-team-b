package at.fhhagenberg.model;

import at.fhhagenberg.service.IElevatorService;

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
        if (speed < 0) {
            speed *= -1;
        }

        this.mSpeed = speed;
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
            System.out.println("Given target is out of the valid range and will not be set!");
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
            System.out.println("Given door status is out of the valid range and will not be set!");
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
            System.out.println("A negative payload for the elevator is invalid and will not be set!");
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
            System.out.println("Given door status is out of the valid range and will not be set!");
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
            System.out.println("Given nearest floor is out of the valid range and will not be set!");
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
            System.out.println("Given floor is out of the valid range and will not be set!");
            return;
        }
        mStops[floorNr] = doStop;
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
}
