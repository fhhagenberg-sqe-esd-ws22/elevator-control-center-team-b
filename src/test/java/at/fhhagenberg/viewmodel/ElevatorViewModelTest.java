package at.fhhagenberg.viewmodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import at.fhhagenberg.logic.BusinessLogic;
import at.fhhagenberg.mock_observable.MockElevatorService;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.model.ModelFactory;
import sqelevator.IElevator;
import at.fhhagenberg.service.IElevatorService;
import at.fhhagenberg.viewmodels.ElevatorViewModel;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ElevatorViewModelTest {
    Elevator model;
    ElevatorViewModel viewModel;
    @Mock
    BusinessLogic logic;

    @BeforeEach
    void setupElevator() {
        MockElevatorService service = new MockElevatorService(1, 2, 10);
        ModelFactory factory = new ModelFactory(service);
        Building building = factory.createBuilding();
        model = building.getElevatorByNumber(0);
        viewModel = new ElevatorViewModel(model, logic);

        model.setAccel(0);
        model.setDirection(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);
        model.setDoorStatus(IElevator.ELEVATOR_DOORS_CLOSED);
        model.setFloor(0);
        model.setPayload(0);
        model.setSpeed(0);
        model.setTarget(0);
    }

    @Test
    void testDoorStatusIntToStr() {
        model.setDoorStatus(IElevator.ELEVATOR_DOORS_CLOSED);
        viewModel.update();
        assertEquals("Closed", viewModel.getDoorStatusStringProp().get());

        model.setDoorStatus(IElevator.ELEVATOR_DOORS_OPEN);
        viewModel.update();
        assertEquals("Open", viewModel.getDoorStatusStringProp().get());

        model.setDoorStatus(IElevator.ELEVATOR_DOORS_CLOSING);
        viewModel.update();
        assertEquals("Closing", viewModel.getDoorStatusStringProp().get());

        model.setDoorStatus(IElevator.ELEVATOR_DOORS_OPENING);
        viewModel.update();
        assertEquals("Opening", viewModel.getDoorStatusStringProp().get());
    }

    @Test
    void testDirectionIntToStr() {
        model.setDirection(IElevator.ELEVATOR_DIRECTION_UP);
        viewModel.update();
        assertEquals(IElevator.ELEVATOR_DIRECTION_UP, viewModel.getDirectionProp().get());

        model.setDirection(IElevator.ELEVATOR_DIRECTION_DOWN);
        viewModel.update();
        assertEquals(IElevator.ELEVATOR_DIRECTION_DOWN, viewModel.getDirectionProp().get());

        model.setDirection(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);
        viewModel.update();
        assertEquals(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED, viewModel.getDirectionProp().get());
    }

    @Test
    void testStops() {
        viewModel.update();
        assertTrue(viewModel.getStopsProp().get().isEmpty());

        model.setStop(0, true);
        viewModel.update();
        assertTrue(viewModel.getStopsProp().get().contains(0));

        model.setStop(1, true);
        viewModel.update();
        assertTrue(viewModel.getStopsProp().get().contains(0));
        assertTrue(viewModel.getStopsProp().get().contains(1));

        model.setStop(0, false);
        viewModel.update();
        assertFalse(viewModel.getStopsProp().get().contains(0));
        assertTrue(viewModel.getStopsProp().get().contains(1));
    }

    @Test
    void testServiced() {
        viewModel.update();

        model.setServiced(0, false);
        viewModel.update();
        assertFalse(viewModel.getServicedProp().get().contains(0));

        viewModel.update();
        assertFalse(viewModel.getServicedProp().get().contains(0));

        model.setServiced(0, true);
        viewModel.update();
        assertTrue(viewModel.getServicedProp().get().contains(0));
    }

    @Test
    void testPayloadProp() {
        model.setPayload(40);
        viewModel.update();
        assertEquals(40, viewModel.getPayloadProp().get());
    }

    @Test
    void testSpeedProp() {
        model.setSpeed(10);
        viewModel.update();
        assertEquals(10, viewModel.getSpeedProp().get());
    }

    @Test
    void testAccelProp() {
        model.setAccel(4);
        viewModel.update();
        assertEquals(4, viewModel.getAccelProp().get());
    }

    @Test
    void testTargetProp() {
        model.setTarget(1);
        viewModel.update();
        assertEquals(1, viewModel.getTargetProp().get());
    }

    @Test
    void testDoorProp() {
        model.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        viewModel.update();
        assertEquals("Open", viewModel.getDoorStatusStringProp().get());
    }

    @Test
    void testSetManualProp() {
        viewModel.getManualProp().set(false);
        assertFalse(viewModel.getManualProp().get());
        
        viewModel.getManualProp().set(true);
        assertTrue(viewModel.getManualProp().get());
        verify(logic).setManual(viewModel.getElevatorNr(), true);
    }

    @Test
    void testManualModeSetTarget() {
        viewModel.getManualProp().set(true);
        viewModel.getManualFloorProp().set(1);
        verify(logic).setElevatorManualTarget(viewModel.getElevatorNr(), 1);

        viewModel.getManualFloorProp().set(0);
        verify(logic).setElevatorManualTarget(viewModel.getElevatorNr(), 0);
    }

    @Test
    void testFloorProp() {
        model.setFloor(1);
        viewModel.update();
        assertEquals(1, viewModel.getFloorProp().get());
    }

    @Test
    void testElevatorNr() {
        viewModel.update();
        assertEquals(0, viewModel.getElevatorNr());
    }

    @Test
    void testNrOfFloors() {
        assertEquals(2, viewModel.getNrFloors());
    }

}
