package at.fhhagenberg.viewmodels;

import at.fhhagenberg.logic.BusinessLogic;
import at.fhhagenberg.model.Building;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds all viewmodels and the {@link BusinessLogic} in order to update them
 */
public class BuildingViewModel {
    private final ArrayList<ElevatorViewModel> mElevators;
    private final ArrayList<FloorViewModel> mFloors;

    /**
     * Constructor of BuildingViewModel
     *
     * @param building model of the building
     * @param logic    BusinesLogic that is controling the elevators of this building
     */
    public BuildingViewModel(Building building, BusinessLogic logic) {
        mElevators = new ArrayList<>();
        mFloors = new ArrayList<>();

        for (var elevator : building.getElevators()) {
            mElevators.add(new ElevatorViewModel(elevator, logic));
        }

        for (var floor : building.getFloors()) {
            mFloors.add(new FloorViewModel(floor));
        }

        update();
    }

    /**
     * Getter for the elevator view models
     *
     * @return ArrayList of the elevator view models
     */
    public List<ElevatorViewModel> getElevatorViewModels() {
        return mElevators;
    }

    /**
     * Getter for the floor view models
     *
     * @return ArrayList of the floor view models
     */
    public List<FloorViewModel> getFloorViewModels() {
        return mFloors;
    }

    /**
     * Updates all models and the ViewModels afterwards
     */
    public void update() {
        // update view models for elevators and floors
        for (var elevator : mElevators) {
            elevator.update();
        }
        for (var floor : mFloors) {
            floor.update();
        }
    }
}
