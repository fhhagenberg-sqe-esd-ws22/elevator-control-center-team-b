package at.fhhagenberg.sqelevator;

import java.rmi.RemoteException;

// control panel of an elevator of a certain floor
public class Floor {
    // elevator service API
    private final IElevator mElevator;
    // number of the elevator serving this floor
    private final int mElevatorNr;
    // number wich represents the floor - 0 is ground floor; 1 ist first floor, etc.
    private final int mFloorNr;
    // true if someone inside the elevator pressed the number of this floor
    private Boolean mElevatorButtonPressed;
    // true if someone pressed the "Up" button on this floor
    private Boolean mWantUp;
    // true if someone pressed the "Down" button on this floor
    private Boolean mWantDown;
    // true if the elevator services this floor; if false -> elevator cannot be called, or target this floor
    private Boolean mIsServiced;

    /**
     * Constructor of Floor
     * @param elevator api for retrieving information about the floor
     * @param elevatorNumber number for identifying the elevator responsible for this object
     * @param floorNr number for identifying this floor
     */
    public Floor(IElevator elevator, int elevatorNumber, int floorNr)
    {
        mElevator = elevator;
        mElevatorNr = elevatorNumber;
        mFloorNr = floorNr;
        mElevatorButtonPressed = false;
        mWantUp = false;
        mWantDown = false;
        mIsServiced = true;
    }

    public int getElevatorNumber() { return mElevatorNr; }

    /**
     * Getter for the floor number
     * @return number of this floor
     */
    public int getFloorNumber()
    {
        return mFloorNr;
    }

    /**
     * Getter for the call upwards button
     * @return boolean if someone called the elevator upwards
     */
    public Boolean getWantUp()
    {
        return mWantUp;
    }

    /**
     * Getter for the call downwards button
     * @return boolean if someone called the elevator downwards
     */
    public Boolean getWantDown()
    {
        return mWantDown;
    }

    /**
     * Getter for information if a lift rider (i find des wort gibts) requested a stop at this floor
     * @return true if someone wants to get off the elevator at this floor
     */
    public Boolean getElevatorButtonPressed()
    {
        return mElevatorButtonPressed;
    }

    /**
     * Getter for information if the floor is currently serviced
     * @return true if this floor can be targeted and the elevator can be called from this floor
     */
    public Boolean isServiced()
    {
        return mIsServiced;
    }
    
    /**
     * Attempts to update the information about the floor
     * @throws RemoteException if the server is unreachable
     */
    public void update() throws RemoteException
    {
        mIsServiced = mElevator.getServicesFloors(mElevatorNr, mFloorNr);
        if (mIsServiced)
        {
            mWantUp = mElevator.getFloorButtonUp(mFloorNr);
            mWantDown = mElevator.getFloorButtonDown(mFloorNr);
            mElevatorButtonPressed = mElevator.getElevatorButton(mElevatorNr, mFloorNr);
        }
        else
        {
            mWantUp = false;
            mWantDown = false;
            mElevatorButtonPressed = false;
        }
    }
}
