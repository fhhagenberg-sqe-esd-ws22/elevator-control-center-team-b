/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;


import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.ModelFactory;
import at.fhhagenberg.service.ElevatorServiceException;
import at.fhhagenberg.service.IElevatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuildingUpdaterTest {
    @Mock IElevatorService service;
    @Mock Building building;

    @Mock FloorUpdater floorUpdater1;
    @Mock FloorUpdater floorUpdater2;
    @Mock FloorUpdater floorUpdater3;

    @Mock ElevatorUpdater elevatorUpdater1;
    @Mock ElevatorUpdater elevatorUpdater2;

    ArrayList<FloorUpdater> floors;
    ArrayList<ElevatorUpdater> elevators;

    @BeforeEach
    void setup() {
        floors = new ArrayList<>();
        floors.add(floorUpdater1);
        floors.add(floorUpdater2);
        floors.add(floorUpdater3);

        elevators = new ArrayList<>();
        elevators.add(elevatorUpdater1);
        elevators.add(elevatorUpdater2);
    }

    @Test
    void testObjectCreationElevatorServiceIsNull() {
        assertThrows(UpdaterException.class, () -> { new BuildingUpdater(null, elevators, floors, building); });
    }

    @Test
    void testObjectCreationNoElevatorUpdaters() {
        var emptyElevators = new ArrayList<ElevatorUpdater>();
        assertThrows(UpdaterException.class, () -> { new BuildingUpdater(service, null, floors, building); });
        assertThrows(UpdaterException.class, () -> { new BuildingUpdater(service, emptyElevators, floors, building); });
    }

    @Test
    void testObjectCreationNoFloorUpdaters() {
        var emptyFloors = new ArrayList<FloorUpdater>();
        assertThrows(UpdaterException.class, () -> { new BuildingUpdater(service, elevators, null, building); });
        assertThrows(UpdaterException.class, () -> { new BuildingUpdater(service, elevators, emptyFloors, building); });
    }

    @Test
    void testObjectCreationNoBuildingModel() {
        assertThrows(UpdaterException.class, () -> { new BuildingUpdater(service, elevators, floors, null); });
    }

    @Test
    void testUpdate() {
        when(service.getFloorNum()).thenReturn(3);
        when(service.getElevatorNum()).thenReturn(2);

        ModelFactory factory = new ModelFactory(service);
        var building = factory.createBuilding();
        BuildingUpdater updater = new BuildingUpdater(service, elevators, floors, building);
        updater.update();

        verify(elevators.get(0)).update();
        verify(elevators.get(1)).update();
        verify(floors.get(0)).update();
        verify(floors.get(1)).update();
        verify(floors.get(2)).update();
    }

    @Test
    void testUpdateFloorsFails() {
        when(service.getFloorNum()).thenReturn(3);
        when(service.getElevatorNum()).thenReturn(2);
        doThrow(ElevatorServiceException.class).when(floorUpdater1).update();

        ModelFactory factory = new ModelFactory(service);
        var building = factory.createBuilding();
        BuildingUpdater updater = new BuildingUpdater(service, elevators, floors, building);

        assertThrows(ElevatorServiceException.class, updater::update);
    }

    @Test
    void testUpdateElevatorsFails() {
        when(service.getFloorNum()).thenReturn(3);
        when(service.getElevatorNum()).thenReturn(2);
        doThrow(ElevatorServiceException.class).when(elevatorUpdater1).update();

        ModelFactory factory = new ModelFactory(service);
        var building = factory.createBuilding();
        BuildingUpdater updater = new BuildingUpdater(service, elevators, floors, building);

        assertThrows(ElevatorServiceException.class, updater::update);
    }
}
