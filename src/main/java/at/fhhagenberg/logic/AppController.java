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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class AppController {

    public static final int UPDATE_INTERVAL_MS = 500;
    public static final int TERMINATION_TIMEOUT_MS = 1000;
    public static final int DISPLAY_MESSAGE_FAILURE_CNT = 5;

    BusinessLogic mLogic;
    BuildingViewModel mViewModel;
    IUpdater mUpdater;

    IElevatorService mService;

    Consumer<String> mDisplayErrorCb;
    Consumer<String> mDisplayInfoCb;

    ScheduledExecutorService mExecutor;

    boolean mDisplayedError;
    int mUpdateFailureCnt;

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

    public void start() {
        Runnable task = () -> Platform.runLater(this::updateCycle);
        mExecutor.scheduleAtFixedRate(task, UPDATE_INTERVAL_MS, UPDATE_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        mExecutor.shutdown();
        try {
            if(!mExecutor.awaitTermination(TERMINATION_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                mExecutor.shutdownNow();
            }
        }
        catch (InterruptedException e) {
            Logging.getLogger().error(e.getMessage());
            mExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void updateCycle() {
        try {
            mUpdater.update();
            mViewModel.update();
            mLogic.setNextTargets();

            if (mDisplayedError) {
                mDisplayedError = false;
                mDisplayInfoCb.accept("The connection to the service got reestablished and the application is running again!");
            }
        }
        catch(Exception ex) {
            Logging.getLogger().error(ex.getMessage());
            handleUpdateError();
        }
    }

    private void handleUpdateError() {
        mUpdateFailureCnt++;
        boolean connected = mService.connect();
        if (!connected && mUpdateFailureCnt == DISPLAY_MESSAGE_FAILURE_CNT) {
            mDisplayErrorCb.accept("The application is not able to connect to the remote service anymore! Please check your connection to the internet and maybe restart the application!");
            mDisplayedError = true;
        }
    }
}
