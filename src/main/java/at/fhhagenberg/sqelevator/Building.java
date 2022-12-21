package at.fhhagenberg.sqelevator;

import java.rmi.RemoteException;
import java.util.ArrayList;

// contains information about all elevators of a building
public class Building {
    /**
     * Constructor of a building
     * @param e API for retrieving information about elevators and floors
     * @throws RemoteException if the server is unreachable
     */
    public Building(IElevator e) throws RemoteException {
        int nrElevators = e.getElevatorNum();
        mElevators = new ArrayList<Elevator>(nrElevators);
        for(int i = 0; i < nrElevators; ++i){
            mElevators.add(new Elevator(e, i));
        }
    }

    /**
     * Getter for all elevator models
     * @return a container of all elevator models
     */
    public ArrayList<Elevator> GetElevators() {
        return mElevators;
    }

    /**
     * Attempts to update the information of all elevators
     * @throws RemoteException if the server is unreachable
     */
    public void Update() throws RemoteException {
        for (Elevator elevator : mElevators) {
            elevator.Update();
        }
    }

    // container of all elevators of the building
    private ArrayList<Elevator> mElevators;
}
