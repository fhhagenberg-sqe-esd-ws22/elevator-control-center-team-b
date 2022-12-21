package at.fhhagenberg.sqelevator;

import java.rmi.RemoteException;
import java.util.ArrayList;

// contains information about all elevators of a building
public class Building {
    // container of all elevators of the building
    private final ArrayList<Elevator> mElevators;

    /**
     * Constructor of a building
     * @param e API for retrieving information about elevators and floors
     * @throws ElevatorException if the object could not get created
     */
    public Building(IElevator e) throws ElevatorException {
        try {
            int nrElevators = e.getElevatorNum();
            int numberOfFloors = e.getFloorNum();
            mElevators = new ArrayList<Elevator>(nrElevators);
            for(int i = 0; i < nrElevators; ++i){
                ArrayList<Floor> floors = createFloors(e, i, numberOfFloors);
                mElevators.add(new Elevator(e, i, floors));
            }
        }
        catch(RemoteException ex) {
            throw new ElevatorException("An error occurred during the creation of the model for the building. \nError message: " + ex.getMessage());
        }
    }

    private ArrayList<Floor> createFloors(IElevator api, int elevatorNumber, int nrOfFloors) {
        ArrayList<Floor> res = new ArrayList<>();
        for (int i = 0; i < nrOfFloors; i++) {
            res.add(new Floor(api, elevatorNumber, i));
        }

        return res;
    }

    /**
     * Getter for all elevator models
     * @return a container of all elevator models
     */
    public ArrayList<Elevator> getElevators() {
        return mElevators;
    }

    /**
     * Attempts to update the information of all elevators
     * @throws ElevatorException if an error occured
     */
    public void update() throws ElevatorException {
        try {
            for (Elevator elevator : mElevators) {
                elevator.update();
            }
        }
        catch(RemoteException ex) {
            throw new ElevatorException("An error occurred while updating the model of the building. \nError message: " + ex.getMessage());
        }
    }
}
