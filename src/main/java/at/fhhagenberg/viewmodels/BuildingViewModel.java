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

        mTimer.schedule(getUpdateTask(), cUpdateInterval); //
    }

    public Vector<ElevatorViewModel> getElevatorViewModels() {
        return mElevators;
    }

    public Vector<FloorViewModel> getFloorViewModels() {
        return mFloors;
    }

    private TimerTask getUpdateTask() {
        return new TimerTask() {
            public void run() {
                update();
            }
        };
    }

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
