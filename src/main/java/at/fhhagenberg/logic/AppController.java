/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.logic;

import at.fhhagenberg.logging.Logging;
import at.fhhagenberg.service.IElevatorService;
import at.fhhagenberg.updater.IUpdater;
import at.fhhagenberg.viewmodels.BuildingViewModel;
import javafx.application.Platform;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * COntroller class for the whole application. Encapsulates the updating mechanism and the error handling (reconnect).
 */
public class AppController {
    /**
     * Update interval in milliseconds
     */
    public static final int UPDATE_INTERVAL_MS = 100;
    /**
     * Timeout for termination of the controller in milliseconds
     */
    public static final int TERMINATION_TIMEOUT_MS = 1000;
    /**
     * At this amount of failures, an alert is shown to the user
     */
    public static final int DISPLAY_MESSAGE_FAILURE_CNT = 5;

    final BusinessLogic mLogic;
    final BuildingViewModel mViewModel;
    final IUpdater mUpdater;

    final IElevatorService mService;

    final Consumer<String> mDisplayErrorCb;
    final Consumer<String> mDisplayInfoCb;

    final ScheduledExecutorService mExecutor;

    boolean mDisplayedError;
    int mUpdateFailureCnt;

    /**
     * Constructor for the AppController
     *
     * @param service    An instance of an ElevatorService to control the remote elevators
     * @param updater    An instance of IUpdater to trigger an update for the GUI
     * @param logic      An instacne of the BusinessLogic object.
     * @param vm         An instacne of the main BuildingViewModel
     * @param executor   An instance of a ScheduledExecutorService to periodically trigger a GUI update and BusinessLogic run.
     * @param showErrCb  A callback to display error messages to the user.
     * @param showInfoCb A callback to display info messages to the user.
     */
    public AppController(IElevatorService service, IUpdater updater, BusinessLogic logic, BuildingViewModel vm, ScheduledExecutorService executor, Consumer<String> showErrCb, Consumer<String> showInfoCb) {
        if (service == null || updater == null || logic == null || vm == null || executor == null || showErrCb == null || showInfoCb == null) {
            throw new LogicException("Could not create AppController, one or more of the given objects are null");
        }

        mLogic = logic;
        mViewModel = vm;
        mUpdater = updater;
        mDisplayErrorCb = showErrCb;
        mDisplayInfoCb = showInfoCb;
        mService = service;
        mUpdateFailureCnt = 0;
        mExecutor = executor;
    }

    /**
     * Creates a task to update the GUI and perform a Businesslogic workflow. Schedules the task periodically.
     */
    public void start() {
        Runnable task = this::updateCycle;
        mExecutor.scheduleAtFixedRate(task, UPDATE_INTERVAL_MS, UPDATE_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the periodic execution of the Task created in start.
     */
    public void stop() {
        mExecutor.shutdown();
        try {
            if (!mExecutor.awaitTermination(TERMINATION_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                mExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Logging.getLogger().error(e.getMessage());
            mExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Updates the models, the GUI and performs a BusinessLogic workflow.
     */
    private void updateCycle() {
        try {
            mUpdater.update();
            Platform.runLater(mViewModel::update);
            mLogic.setNextTargets();

            if (mDisplayedError) {
                mDisplayedError = false;
                mUpdateFailureCnt = 0;
                Platform.runLater(() -> mDisplayInfoCb.accept("The connection to the service got reestablished and the application is running again!"));
            }
        } catch (Exception ex) {
            Logging.getLogger().error(ex.getMessage());
            handleUpdateError();
        }
    }

    /**
     * Processes an occurred excpetion during an update cycle.
     */
    private void handleUpdateError() {
        mUpdateFailureCnt++;
        boolean connected = mService.connect();
        if (!connected && mUpdateFailureCnt == DISPLAY_MESSAGE_FAILURE_CNT) {
            Platform.runLater(() -> mDisplayErrorCb.accept("The application is not able to connect to the remote service anymore! Please check your connection to the internet and maybe restart the application!"));
            mDisplayedError = true;
        }
    }
}
