package at.fhhagenberg.viewmodels;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import at.fhhagenberg.logic.BusinesLogic;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.updater.BuildingUpdater;

public class BuildingViewModel {
    private final BuildingUpdater mUpdater;
    private final Building mBuilding;
    private final Vector<ElevatorViewModel> mElevators;
    private final Vector<FloorViewModel> mFloors;
    private final Timer mTimer;

    // update interval in [ms]
    static private final int cUpdateInterval = 1000;

    /**
     * Constructor of BuildingViewModel
     * @param updater updater for the building
     * @param building model of the building
     * @param logic BusinesLogic that is controling the elevators of this building
     */
    public BuildingViewModel(BuildingUpdater updater, Building building, BusinesLogic logic) {
        mUpdater = updater;
        mBuilding = building;
        mElevators = new Vector<>(mBuilding.getElevators().size());
        mFloors = new Vector<>(mBuilding.getFloors().size());
        mTimer = new Timer();

        for (var elevator : building.getElevators()) {
            mElevators.add(new ElevatorViewModel(elevator, logic));
        }

        for (var floor : building.getFloors()) {
            mFloors.add(new FloorViewModel(floor));
        }

        update();

        mTimer.schedule(getUpdateTask(), cUpdateInterval);
    }

    /**
     * Getter for the elevator view models
     * @return vector of the elevator view models
     */
    public Vector<ElevatorViewModel> getElevatorViewModels() {
        return mElevators;
    }

    /**
     * Getter for the floor view models
     * @return vector of the floor view models
     */
    public Vector<FloorViewModel> getFloorViewModels() {
        return mFloors;
    }

    /**
     * Getter for the task that calls this.update()
     * @return task that calls this.update()
     */
    private TimerTask getUpdateTask() {
        return new TimerTask() {
            public void run() {
                update();
            }
        };
    }

    /**
     * Updates all models and the ViewModels afterwards
     */
    private void update() {
        mUpdater.update();
        for (var elevator : mElevators) {
            elevator.update();
        }
        for (var floor : mFloors) {
            floor.update();
        }

        mTimer.schedule(getUpdateTask(), cUpdateInterval);
    }
}
