package at.fhhagenberg.sqelevator;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Building {

    public Building(IElevator e) throws RemoteException {
        int nrElevators = e.getElevatorNum();
        mElevators = new ArrayList<Elevator>(nrElevators);
        for(int i = 0; i < nrElevators; ++i){
            mElevators.add(new Elevator());
        }

        int nrFloors = e.getFloorNum();
        mFloors = new ArrayList<Floor>(nrFloors);
        for(int i = 0; i < nrFloors; ++i){
            mFloors.add(new Floor());
        }
    }

    private ArrayList<Floor> mFloors;
    private ArrayList<Elevator> mElevators;
}
