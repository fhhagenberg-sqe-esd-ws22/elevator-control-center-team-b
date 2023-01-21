/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.model.Floor;
import at.fhhagenberg.service.IElevatorService;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory that creates all updaters of a building via a {@link IElevatorService}
 */
public class UpdaterFactory {
    private final IElevatorService mElevatorService;

    /**
     * Constructor of the UpdaterFactory
     * @param service IElevatorService object which will be passed to the updaters in order to perform the API requests for the updates.
     */
    public UpdaterFactory(IElevatorService service) {
        if (service == null) {
            throw new UpdaterException("Could not create updater factory, because the given IElevatorService is null");
        }

        mElevatorService = service;
    }

    /**
     * Creates a BuildingUpdater for the given Building object.
     * @param building Building object which shall be updated.
     * @return BuildingUpdater for the given Building object.
     */
    public BuildingUpdater createBuildingUpdater(Building building) {
        if (building == null) {
            throw new UpdaterException("Could not create BuildingUpdater, since there is no Building given!");
        }

        return new BuildingUpdater(mElevatorService, createElevatorUpdaters(building.getElevators()), createFloorUpdaters(building.getFloors()), building);
    }

    /**
     * Creates an ElevatorUpdater object for every given Elevator object.
     * @param elevators List of elevators which shall be updated.
     * @return List of ElevatorUpdaters for the given Elevator objects.
     */
    private List<ElevatorUpdater> createElevatorUpdaters(List<Elevator> elevators) {
        if (elevators == null || elevators.isEmpty()) {
            throw new UpdaterException("Could not create ElevatorUpdaters, since there are no Elevators given!");
        }

        var updaters = new ArrayList<ElevatorUpdater>();
        for (Elevator e: elevators) {
            updaters.add(new ElevatorUpdater(mElevatorService, e));
        }

        return updaters;
    }

    /**
     * Creates an FloorUpdater object for every given Floor object.
     * @param floors List of floors which shall be updated.
     * @return List of FloorUpdaters for the given Floor objects.
     */
    private List<FloorUpdater> createFloorUpdaters(List<Floor> floors) {
        if (floors == null || floors.isEmpty()) {
            throw new UpdaterException("Could not create FloorUpdaters, since there are no Elevators given!");
        }

        var updaters = new ArrayList<FloorUpdater>();
        for (Floor f: floors) {
            updaters.add(new FloorUpdater(mElevatorService, f));
        }

        return updaters;
    }
}
