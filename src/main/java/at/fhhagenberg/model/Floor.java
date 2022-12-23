package at.fhhagenberg.model;

import at.fhhagenberg.service.IElevator;

import java.rmi.RemoteException;

// control panel of an elevator of a certain floor
public class Floor {
    // number wich represents the floor - 0 is ground floor; 1 ist first floor, etc.
    private final int mFloorNr;

    // true if someone pressed the "Up" button on this floor
    private Boolean mWantUp;
    // true if someone pressed the "Down" button on this floor
    private Boolean mWantDown;
    // true if the elevator services this floor; if false -> elevator cannot be called, or target this floor

    /**
     * Constructor of Floor
     * @param floorNr number for identifying this floor
     */
    public Floor(int floorNr)
    {
        mFloorNr = floorNr;
        mWantUp = false;
        mWantDown = false;
    }

    /**
     * Setter for the WantUp flag
     * @param wantUp True if the WantUp button on the floor is pressed, otherwise false
     */
    public void setWantUp(Boolean wantUp) {
        this.mWantUp = wantUp;
    }

    /**
     * Setter for the WantDown flag
     * @param wantDown True if the WantDown button on the floor is pressed, otherwise false
     */
    public void setWantDown(Boolean wantDown) {
        this.mWantDown = wantDown;
    }

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
}
