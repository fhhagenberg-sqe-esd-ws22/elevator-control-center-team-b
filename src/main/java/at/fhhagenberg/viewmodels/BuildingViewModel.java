package at.fhhagenberg.viewmodels;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import at.fhhagenberg.logic.BusinesLogic;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.updater.BuildingUpdater;
import javafx.application.Platform;

public class BuildingViewModel {
    private final BuildingUpdater mUpdater;
    private final ArrayList<ElevatorViewModel> mElevators;
    private final ArrayList<FloorViewModel> mFloors;
    private final Timer mTimer;

    // update interval in [ms]
    private static final int UPDATE_INTERVAL = 100;

    /**
     * Constructor of BuildingViewModel
     * @param updater updater for the building
     * @param building model of the building
     * @param logic BusinesLogic that is controling the elevators of this building
     */
    public BuildingViewModel(BuildingUpdater updater, Building building, BusinesLogic logic) {
        mUpdater = updater;
        mElevators = new ArrayList<>();
        mFloors = new ArrayList<>();
        mTimer = new Timer();

        for (var elevator : building.getElevators()) {
            mElevators.add(new ElevatorViewModel(elevator, logic));
        }

        for (var floor : building.getFloors()) {
            mFloors.add(new FloorViewModel(floor));
        }

        update();

        mTimer.schedule(getUpdateTask(), UPDATE_INTERVAL);
    }

    /**
     * Getter for the elevator view models
     * @return ArrayList of the elevator view models
     */
    public List<ElevatorViewModel> getElevatorViewModels() {
        return mElevators;
    }

    /**
     * Getter for the floor view models
     * @return ArrayList of the floor view models
     */
    public List<FloorViewModel> getFloorViewModels() {
        return mFloors;
    }

    /**
     * Getter for the task that calls this.update()
     * @return task that calls this.update()
     */
    private TimerTask getUpdateTask() {
        return new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });;
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

        mTimer.schedule(getUpdateTask(), UPDATE_INTERVAL);
    }
}
