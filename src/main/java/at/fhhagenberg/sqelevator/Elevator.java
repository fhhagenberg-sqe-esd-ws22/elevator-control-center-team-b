package at.fhhagenberg.sqelevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

// contains all information of a single elevator
public class Elevator {
    // elevator service API
    private final IElevator mElevator;
    // number of this elevator
    private final int mElevatorNr;
    // all floors this elevator can access
    private final ArrayList<Floor> mFloors;
    // height of a single floor
    private final int mFloorHeight;
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
     * @param elevator api for retrieving information about the elevator
     * @param elevatorNr number for identifying this elevator
     * @throws ElevatorException if the creation of the object failed
     */
    public Elevator(IElevator elevator, int elevatorNr, ArrayList<Floor> floors) throws ElevatorException
    {
        if (floors == null || floors.isEmpty()) {
            throw new ElevatorException("Can not create Elevatro object with empty or null floor list!");
        }

        mElevator = elevator;
        mFloorHeight = floors.size();
        mFloors = floors;
        mElevatorNr = elevatorNr;
        mSpeed = 0;
        mAccel = 0;
        mTarget = 0;
        mPayload = 0;
        mDirection = IElevator.ELEVATOR_DIRECTION_UNCOMMITTED;
        mDoorStatus = IElevator.ELEVATOR_DOORS_CLOSED;
        mNearestFloor = 0;
    }

    /**
     * Getter function for thefloors of an elevator.
     * @return List of Floor objects.
     */
    public ArrayList<Floor> getFloors() {
        return  mFloors;
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
     * Attempts to update the information of the elevator
     * @throws RemoteException if the server is unreachable
     */
    public void update() throws RemoteException
    {
        mSpeed = mElevator.getElevatorSpeed(mElevatorNr);
        // we have a direction - no negative speeds needed
        if (mSpeed < 0) {
            mSpeed *= -1;
        }
        // negative accelleration in upwards speed is possible - no need for abs
        mAccel = mElevator.getElevatorAccel(mElevatorNr);
        int target = mElevator.getTarget(mElevatorNr);
        if (target < 0) {
            target = 0; //TODO(PH): what about underground floors?
        }
        else if (target > mFloors.size()) {
            target = mFloors.size() - 1;
        }
        mTarget = target;
        int direction = mElevator.getCommittedDirection(mElevatorNr);
        if (direction == IElevator.ELEVATOR_DIRECTION_UP ||
          direction == IElevator.ELEVATOR_DIRECTION_DOWN ||
          direction == IElevator.ELEVATOR_DIRECTION_UNCOMMITTED) { //TODO(PH): either proof integer range and throw an exception if wrong or just addign the value
            mDirection = direction;
        }
        // no max weight specified
        int weight = mElevator.getElevatorWeight(mElevatorNr);
        if (weight >= 0) {
            mPayload = weight;
        }
        int doorStatus = mElevator.getElevatorDoorStatus(mElevatorNr);
        if (doorStatus == IElevator.ELEVATOR_DOORS_CLOSED ||
          doorStatus == IElevator.ELEVATOR_DOORS_CLOSING ||
          doorStatus == IElevator.ELEVATOR_DOORS_OPEN ||
          doorStatus == IElevator.ELEVATOR_DOORS_OPENING) {
            mDoorStatus = doorStatus;
        }
        calcNearestFloor();

        for (Floor floor : mFloors)
        {
            floor.update();
        }
    }

    /**
     * Updates mNearestFloor
     * @throws RemoteException if the server is unreachable
     */
    private void calcNearestFloor() throws RemoteException
    {
        int height = mElevator.getElevatorPosition(mElevatorNr);

        int floor = height / mFloorHeight;
        if (height % mFloorHeight > mFloorHeight / 2)
        {
            floor++;
        }

        if (floor >= mFloors.size()) {
            floor = mFloors.size() - 1;
        }

        mNearestFloor = floor;
    }
}
