package at.fhhagenberg.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import at.fhhagenberg.logic.BusinesLogic;
import at.fhhagenberg.mock_observable.MockElevatorService;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.model.Floor;
import at.fhhagenberg.model.ModelFactory;
import at.fhhagenberg.updater.BuildingUpdater;
import at.fhhagenberg.updater.ElevatorUpdater;
import at.fhhagenberg.updater.FloorUpdater;
import at.fhhagenberg.viewmodels.BuildingViewModel;

class BuildingViewModelTest {
    static Building model;
    static BuildingViewModel viewModel;
    static BusinesLogic logic;
    static BuildingUpdater updater;
    static MockElevatorService service;

    @BeforeAll
    static void setup() {
        service = new MockElevatorService(2, 2, 10);
        ModelFactory factory = new ModelFactory(service);
        model = factory.createBuilding();
        logic = new BusinesLogic(model);
        
        List<FloorUpdater> floorUpdaters = new ArrayList<>();
        List<ElevatorUpdater> elevatorUpdaters = new ArrayList<>();

        for (Elevator elevator : model.getElevators()) {
            elevatorUpdaters.add(new ElevatorUpdater(service, elevator));
        }

        for (Floor floor : model.getFloors()) {
            floorUpdaters.add(new FloorUpdater(service, floor));
        }

        updater = new BuildingUpdater(service, elevatorUpdaters, floorUpdaters, model);
        viewModel = new BuildingViewModel(updater, model, logic);
    }



    @Test
    void testUpdate() {
        assertEquals(0, model.getElevatorByNumber(0).getSpeed());
        assertEquals(0, viewModel.getElevatorViewModels().get(0).getSpeed());
        assertEquals(0, model.getElevatorByNumber(1).getSpeed());
        assertEquals(0, viewModel.getElevatorViewModels().get(1).getSpeed());
        assertFalse(model.getFloorByNumber(0).getWantUp());
        assertFalse(viewModel.getFloorViewModels().get(0).getWantUp());
        assertFalse(logic.getManual(0));

        service.setSpeed(0, 10);
        service.setSpeed(1, 20);
        service.setFloorUp(0, true);
        try {
            Thread.sleep(1100);
        }
        catch (InterruptedException ex) {
            
        }

        assertEquals(10, model.getElevatorByNumber(0).getSpeed());
        assertEquals(10, viewModel.getElevatorViewModels().get(0).getSpeed());
        assertEquals(20, model.getElevatorByNumber(1).getSpeed());
        assertEquals(20, viewModel.getElevatorViewModels().get(1).getSpeed());
        assertTrue(model.getFloorByNumber(0).getWantUp());
        assertTrue(viewModel.getFloorViewModels().get(0).getWantUp());
    }
}
