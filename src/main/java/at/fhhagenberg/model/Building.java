package at.fhhagenberg.model;

import java.util.Comparator;
import java.util.List;

// contains information about all elevators of a building
public class Building {
    private final List<Elevator> mElevators;

    private final List<Floor> mFloors;

    private final boolean[][] mElevatorToFloorServiceMapping;

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
        mElevatorToFloorServiceMapping = new boolean[elevators.size()][floors.size()];
        elevators.sort(new SortElevatorByNumberAsc()); //lists must be sorted in order to provide a valid service mapping
        floors.sort(new SortFloorByNumberAsc());

        for (int i = 0; i < elevators.size(); ++i) {
            for (int j = 0; j < floors.size(); ++j) {
                mElevatorToFloorServiceMapping[i][j] = true; //initially every floor gets serviced by every elevator
            }
        }
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
     * Function to specify wether a floor is serviced by an elevator or not. If any given parameter is invalid, no value gets changed.
     * @param elevatorNr Number to identify the desired elevator.
     * @param floorNr Number to identify the desired floor.
     * @param service True if the floor shall be serviced by the elevator, otherwise false.
     */
    public void setIsServiced(int elevatorNr, int floorNr, Boolean service) {
        int elevatorIdx = getElevatorIdxByNumber(elevatorNr);
        int floorIdx = getFloorIdxByNumber(floorNr);

        if (elevatorIdx == -1 || floorIdx == -1) {
            System.out.println("Could not find specified elevator or floor");
            return;
        }

        mElevatorToFloorServiceMapping[elevatorIdx][floorIdx] = service;
    }

    /**
     * Function to check wether a floor is serviced by an elevator.
     * @param elevatorNr Number to identify the desired elevator.
     * @param floorNr Number to identify the desired floor.
     * @return True if the floor is serviced by the elevator, false if not or any of the given parameters are invalid.
     */
    public boolean getIsServiced(int elevatorNr, int floorNr) {
        int elevatorIdx = getElevatorIdxByNumber(elevatorNr);
        int floorIdx = getFloorIdxByNumber(floorNr);

        if (elevatorIdx == -1 || floorIdx == -1) {
            System.out.println("Could not find specified elevator or floor");
            return false;
        }

        return mElevatorToFloorServiceMapping[elevatorIdx][floorIdx];
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

    /**
     * Function to get the list index of an elevator object specified by its elevator number.
     * @param elevatorNumber Elevator number of the elevator object
     * @return Index of an elevator object specified by its elevator number, -1 if no element with the given elevator number has been found.
     */
    private int getElevatorIdxByNumber(int elevatorNumber) {
        for (int i = 0; i < mElevators.size(); ++i) {
            if (mElevators.get(i).getElevatorNr() == elevatorNumber) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Function to get the list index of a floor object specified by its floor number.
     * @param floorNumber Floor number of the floor object.
     * @return Index of a floor object specified by its floor number, -1 if no element with the given floor number has been found.
     */
    private int getFloorIdxByNumber(int floorNumber) {
        for (int i = 0; i < mFloors.size(); ++i) {
            if (mFloors.get(i).getFloorNumber() == floorNumber) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Comparator object to sort the floor list ascending by the floor number
     */
    private static class SortFloorByNumberAsc implements Comparator<Floor> {
        @Override
        public int compare(Floor o1, Floor o2) {
            return o1.getFloorNumber() - o2.getFloorNumber();
        }
    }

    /**
     * Comparator object to sort the elevator list ascending by the floor number
     */
    private static class SortElevatorByNumberAsc implements Comparator<Elevator> {
        @Override
        public int compare(Elevator o1, Elevator o2) {
            return o1.getElevatorNr() - o2.getElevatorNr();
        }
    }

}
