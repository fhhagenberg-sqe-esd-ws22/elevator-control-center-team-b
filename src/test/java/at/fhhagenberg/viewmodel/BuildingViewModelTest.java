package at.fhhagenberg.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import at.fhhagenberg.logic.BusinessLogic;
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
    Building model;
    BuildingViewModel viewModel;
    BusinessLogic logic;
    BuildingUpdater updater;
    MockElevatorService service;
    TimerSetable timer;

    @BeforeEach
    void setup() {
        service = new MockElevatorService(2, 2, 10);
        ModelFactory factory = new ModelFactory(service);
        model = factory.createBuilding();
        logic = new BusinessLogic(model);
        
        List<FloorUpdater> floorUpdaters = new ArrayList<>();
        List<ElevatorUpdater> elevatorUpdaters = new ArrayList<>();

        for (Elevator elevator : model.getElevators()) {
            elevatorUpdaters.add(new ElevatorUpdater(service, elevator));
        }

        for (Floor floor : model.getFloors()) {
            floorUpdaters.add(new FloorUpdater(service, floor));
        }

        timer = new TimerSetable();
        updater = new BuildingUpdater(service, elevatorUpdaters, floorUpdaters, model);
        viewModel = new BuildingViewModel(updater, model, logic, timer);
    }

    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(() -> semaphore.release());
        semaphore.acquire();
    }



    @Disabled
    void testUpdate() throws InterruptedException {
        assertEquals(0, model.getElevatorByNumber(0).getSpeed());
        assertEquals(0, model.getElevatorByNumber(1).getSpeed());
        assertFalse(model.getFloorByNumber(0).getWantUp());
        assertFalse(viewModel.getFloorViewModels().get(0).getWantUp());
        assertFalse(model.getFloorByNumber(0).getWantDown());
        assertFalse(viewModel.getFloorViewModels().get(0).getWantDown());
        assertFalse(logic.getManual(0));

        service.setSpeed(0, 10);
        service.setSpeed(1, 20);
        service.setFloorUp(0, true);

        // TODO: ask during lesson what is wrong here
        // passes individually, but fails if run with other tests
        // needed so the platform is initialized, as we call platform.runLater in the Thread
        Platform.startup(()->{});
        timer.forceUpdate();
        // waits for runLater to have finished
        waitForRunLater();

        assertEquals(10, model.getElevatorByNumber(0).getSpeed());
        assertEquals(20, model.getElevatorByNumber(1).getSpeed());
        assertTrue(model.getFloorByNumber(0).getWantUp());
        assertTrue(viewModel.getFloorViewModels().get(0).getWantUp());
        assertFalse(model.getFloorByNumber(0).getWantDown());
        assertFalse(viewModel.getFloorViewModels().get(0).getWantDown());
    }
}
