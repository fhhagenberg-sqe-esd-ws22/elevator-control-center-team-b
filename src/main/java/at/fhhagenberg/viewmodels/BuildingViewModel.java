package at.fhhagenberg.viewmodels;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import at.fhhagenberg.logging.Logging;
import at.fhhagenberg.logic.BusinessLogic;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.ModelException;
import at.fhhagenberg.updater.BuildingUpdater;
import at.fhhagenberg.updater.UpdaterException;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Class that holds all viewmodels and the {@link BusinessLogic} in order to update them
 */
public class BuildingViewModel {
    private final BuildingUpdater mUpdater;
    private final BusinessLogic mLogic;
    private final ArrayList<ElevatorViewModel> mElevators;
    private final ArrayList<FloorViewModel> mFloors;
    private final Timer mTimer;
    private boolean mShowedError;

    // update interval in [ms]
    private static final int UPDATE_INTERVAL = 500;

    /**
     * Constructor of BuildingViewModel
     * @param updater {@link BuildingUpdater} for the building
     * @param building {@link Building} model of the physical building
     * @param logic {@link BusinessLogic} that is controling the elevators of this building
     * @param timer The {@link Timer} responsible for triggering an update task
     */
    public BuildingViewModel(BuildingUpdater updater, Building building, BusinessLogic logic, Timer timer) {
        mUpdater = updater;
        mLogic = logic;
        mElevators = new ArrayList<>();
        mFloors = new ArrayList<>();
        mTimer = timer;
        mShowedError = false;

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
                });
            }
        };
    }

    /**
     * Updates all models and the ViewModels afterwards
     */
    private void update() {
        try {
            // update building
            mUpdater.update();

            // update view models for elevators and floors
            for (var elevator : mElevators) {
                elevator.update();
            }
            for (var floor : mFloors) {
                floor.update();
            }

            mLogic.setNextTargets();
        }
        catch(UpdaterException ex) {
            Logging.getLogger().error(ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Critical Error Occurred");
            alert.setContentText(ex.getMessage());
            alert.show();
        }
        catch (ModelException ex) {
            Logging.getLogger().error(ex.getMessage());
        }

        mTimer.schedule(getUpdateTask(), UPDATE_INTERVAL);
    }
}
