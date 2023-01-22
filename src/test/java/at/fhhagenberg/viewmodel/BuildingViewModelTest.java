package at.fhhagenberg.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import at.fhhagenberg.service.IElevatorService;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class BuildingViewModelTest {
    Building model;
    BuildingViewModel viewModel;
    @Mock
    BusinessLogic logic;
    @Mock
    IElevatorService service;

    @BeforeEach
    void setup() {
        when(service.getFloorNum()).thenReturn(5);
        when(service.getElevatorNum()).thenReturn(3);
        ModelFactory factory = new ModelFactory(service);
        model = factory.createBuilding();
        logic = new BusinessLogic(model);
        viewModel = new BuildingViewModel(model, logic);
    }

    @Test
    void testUpdate() {
        assertEquals(0, model.getElevatorByNumber(0).getSpeed());
        assertEquals(0, model.getElevatorByNumber(1).getSpeed());
        assertFalse(model.getFloorByNumber(0).getWantUp());
        assertFalse(viewModel.getFloorViewModels().get(0).getWantUp());
        assertFalse(model.getFloorByNumber(0).getWantDown());
        assertFalse(viewModel.getFloorViewModels().get(0).getWantDown());
        assertFalse(logic.getManual(0));

        model.getElevators().get(0).setSpeed(10);
        model.getElevators().get(1).setSpeed(20);
        model.getFloors().get(0).setWantUp(true);

        viewModel.update();

        assertEquals(10, model.getElevatorByNumber(0).getSpeed());
        assertEquals(20, model.getElevatorByNumber(1).getSpeed());
        assertTrue(model.getFloorByNumber(0).getWantUp());
        assertTrue(viewModel.getFloorViewModels().get(0).getWantUp());
        assertFalse(model.getFloorByNumber(0).getWantDown());
        assertFalse(viewModel.getFloorViewModels().get(0).getWantDown());
    }
}
