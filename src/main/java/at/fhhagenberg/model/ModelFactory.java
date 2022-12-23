/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.model;

import at.fhhagenberg.service.ElevatorServiceException;
import at.fhhagenberg.service.IElevatorService;

import java.util.ArrayList;

public class ModelFactory {
    private final IElevatorService mElevatorService;

    /**
     * Constructor for the ModelFactory.
     * @param service IElevatorService to retrieve the necessary information needed for the build up.
     */
    public ModelFactory(IElevatorService service) {
        if (service == null) {
            throw new ModelException("Could not create model factory, because the given IElevatorService is null");
        }

        mElevatorService = service;
    }

    /**
     * Creates a building objects wih the given floors and elevators.
     * @param elevators Elevators of the building.
     * @param floors Floors of the building.
     * @return Building object.
     */
    public Building createBuilding() {
        return new Building(createElevators(), createFloors());
    }

    /**
     * Creates floor objects which represent the floors of a building.
     * @return List of floor objects which represent the floors of a building.
     */
    private ArrayList<Floor> createFloors() {
        try{
            ArrayList<Floor> floors = new ArrayList<>();
            int nrOfFloors = mElevatorService.getFloorNum();
            for(int i = 0; i < nrOfFloors; ++i) {
                floors.add(new Floor(i));
            }

            return floors;
        }
        catch(ElevatorServiceException ex) {
            throw new ModelException("Could not create floor objects, the following error occurred: \n" + ex.getMessage());
        }
    }

    /**
     * Creates elevator objects which represent the elevators of a building.
     * @return List of elevator objects which represent the elevators of a building.
     */
    private ArrayList<Elevator> createElevators() {
        try{
            ArrayList<Elevator> elevators = new ArrayList<>();
            int nrOfFloors = mElevatorService.getFloorNum();
            int nrOfElevators = mElevatorService.getElevatorNum();
            for(int i = 0; i < nrOfElevators; ++i) {
                elevators.add(new Elevator(i, nrOfFloors));
            }

            return elevators;
        }
        catch(ElevatorServiceException ex) {
            throw new ModelException("Could not create floor objects, the following error occurred: \n" + ex.getMessage());
        }
    }
}
