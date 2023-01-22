/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

import at.fhhagenberg.service.IElevatorService;

/**
 * Base class of all updaters
 */
public abstract class UpdaterBase implements IUpdater {
    /**
     * service used for retrieving update values
     */
    protected final IElevatorService mElevatorService;

    /**
     * Constructor of an {@link UpdaterBase}
     *
     * @param elevatorService service used for retrieving update values
     */
    protected UpdaterBase(IElevatorService elevatorService) {
        if (elevatorService == null) {
            throw new UpdaterException("Could not create Updater object since the given IElevatorService object is null!");
        }
        mElevatorService = elevatorService;
    }
}
