/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

import at.fhhagenberg.service.IElevatorService;

public abstract class UpdaterBase implements IUpdater {
    protected final IElevatorService mElevatorService;

    protected UpdaterBase(IElevatorService elevatorService) {
        if (elevatorService == null) {
            throw new UpdaterException("Could not create Updater object since the given IElevatorService object is null!");
        }
        mElevatorService = elevatorService;
    }
}
