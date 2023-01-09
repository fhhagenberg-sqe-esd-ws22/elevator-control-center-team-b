package at.fhhagenberg.model;

import java.util.List;

// contains information about all elevators of a building
public class Building {
    private final List<Elevator> mElevators;

    private final List<Floor> mFloors;


    /**
     * Constructor of a building
     * @param elevators List of Elevator objects which represent all eleveators in the building
     * @param floors List of Floor objects which represent all floors in the building
     */
    public Building(List<Elevator> elevators, List<Floor> floors) {
        if (floors == null || elevators == null || elevators.isEmpty() || floors.isEmpty()) {
            throw new ModelException("Could not create building, since either no elevators or no floors where given at construction!");
        }
        mElevators = elevators;
        mFloors = floors;
    }

    /**
     * Getter for all elevator models
     * @return a container of all elevator models
     */
    public List<Elevator> getElevators() {
        return mElevators;
    }

    /**
     * Getter for all Floor models
     * @return A container of all Floor models.
     */
    public List<Floor> getFloors() {
        return mFloors;
    }

    /**
     * Function to get a single Elevator object, specified by its floor number.
     * @param elevatorNr Elevator number of the elevator object.
     * @return Elevator object, specified by its elevator number, null if the elevator number doesn't exist.
     */
    public Elevator getElevatorByNumber(int elevatorNr) {
        for (Elevator e : mElevators) {
            if (e.getElevatorNr() == elevatorNr) {
                return e;
            }
        }

        return null;
    }

    /**
     * Function to get a single Floor object, specified by its floor number.
     * @param floorNr Floor number of the floor object
     * @return Floor object, specified by its floor number, null if the floor number doesn't exist.
     */
    public Floor getFloorByNumber(int floorNr) {
        for (Floor f : mFloors) {
            if (f.getFloorNumber() == floorNr) {
                return f;
            }
        }

        return null;
    }


}
