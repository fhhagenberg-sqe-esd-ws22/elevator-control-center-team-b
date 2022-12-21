package at.fhhagenberg.sqelevator;

import java.rmi.RemoteException;
import java.util.ArrayList;

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
     * @throws RemoteException if the server is unreachable
     */
    public Elevator(IElevator elevator, int elevatorNr) throws RemoteException
    {
        mElevator = elevator;
        mElevatorNr = elevatorNr;
        mFloorHeight = mElevator.getFloorHeight();
        mSpeed = 0;
        mAccel = 0;
        mTarget = 0;
        mPayload = 0;
        mDirection = IElevator.ELEVATOR_DIRECTION_UNCOMMITTED;
        mDoorStatus = IElevator.ELEVATOR_DOORS_CLOSED;
        mNearestFloor = 0;
        int nrFloors = mElevator.getFloorNum();
        mFloors = new ArrayList<Floor>(nrFloors);
        for(int i = 0; i < nrFloors; ++i){
            mFloors.add(new Floor(elevator, elevatorNr, i));
        }
    }

    /**
     * Getter for current speed of the elevator
     * @return current speed
     */
    public int GetSpeed() {
        return mSpeed;
    }

    /**
     * Getter for current accelleration of the elevator
     * @return current acceleration
     */
    public int GetAccel() {
        return mAccel;
    }

    /**
     * Getter for the current target floor of the elevator
     * @return next planned stop of the elevator
     */
    public int GetTarget() {
        return mTarget;
    }

    /**
     * Getter for the current direction of the elevator
     * @return current direction of the elevator
     */
    public int GetDirection() {
        return mDirection;
    }

    /**
     * Getter for the current payload of the elevator
     * @return current payload of the elevator
     */
    public int GetPayload() {
        return mPayload;
    }

    /**
     * Getter for the current status of the elevator door
     * @return current status of the elevator door
     */
    public int GetDoorStatus() {
        return mDoorStatus;
    }

    /**
     * Getter for the nearest floor of the elevator
     * @return nearest floor of the elevator
     */
    public int GetNearestFloor() {
        return mNearestFloor;
    }

    /**
     * Attempts to update the information of the elevator
     * @throws RemoteException if the server is unreachable
     */
    public void Update() throws RemoteException
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
            target = 0;
        }
        else if (target > mFloors.size()) {
            target = mFloors.size() - 1;
        }
        mTarget = target;
        int direction = mElevator.getCommittedDirection(mElevatorNr);
        if (direction == IElevator.ELEVATOR_DIRECTION_UP ||
          direction == IElevator.ELEVATOR_DIRECTION_DOWN ||
          direction == IElevator.ELEVATOR_DIRECTION_UNCOMMITTED) {
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
        CalcNearestFloor();

        for (Floor floor : mFloors) 
        {
            floor.Update();
        }
    }

    /**
     * Updates mNearestFloor
     * @throws RemoteException if the server is unreachable
     */
    private void CalcNearestFloor() throws RemoteException
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
