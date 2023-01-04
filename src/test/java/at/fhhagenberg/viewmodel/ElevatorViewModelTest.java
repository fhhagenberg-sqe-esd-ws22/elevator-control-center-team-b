package at.fhhagenberg.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import at.fhhagenberg.logic.BusinesLogic;
import at.fhhagenberg.mock_observable.MockElevatorService;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.model.ModelFactory;
import at.fhhagenberg.service.IElevator;
import at.fhhagenberg.service.IElevatorService;
import at.fhhagenberg.viewmodels.ElevatorViewModel;

class ElevatorViewModelTest {
    static Elevator model;
    static ElevatorViewModel viewModel;
    static BusinesLogic logic;

    @BeforeAll
    static void setup() {
        MockElevatorService service = new MockElevatorService(1, 2, 10);
        ModelFactory factory = new ModelFactory(service);
        Building building = factory.createBuilding();
        model = building.getElevatorByNumber(0);
        logic = new BusinesLogic(building);
        viewModel = new ElevatorViewModel(model, logic);
    }

    @BeforeEach
    void setupElevator() {
        model.setAccel(0);
        model.setDirection(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);
        model.setDoorStatus(IElevator.ELEVATOR_DOORS_CLOSED);
        model.setNearestFloor(0);
        model.setPayload(0);
        model.setSpeed(0);
        model.setTarget(0);
    }

    @Test
    void testDoorStatusIntToStr() {
        model.setDoorStatus(IElevator.ELEVATOR_DOORS_CLOSED);
        viewModel.update();
        assertEquals("Closed", viewModel.getDoorStatusString());
        assertEquals("Closed", viewModel.getDoorStatusStringProp().get());
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSED, viewModel.getDoorStatus());
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSED, viewModel.getDoorStatusProp().get());

        model.setDoorStatus(IElevator.ELEVATOR_DOORS_OPEN);
        viewModel.update();
        assertEquals("Open", viewModel.getDoorStatusString());
        assertEquals("Open", viewModel.getDoorStatusStringProp().get());
        assertEquals(IElevator.ELEVATOR_DOORS_OPEN, viewModel.getDoorStatus());
        assertEquals(IElevator.ELEVATOR_DOORS_OPEN, viewModel.getDoorStatusProp().get());

        model.setDoorStatus(IElevator.ELEVATOR_DOORS_CLOSING);
        viewModel.update();
        assertEquals("Closing", viewModel.getDoorStatusString());
        assertEquals("Closing", viewModel.getDoorStatusStringProp().get());
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSING, viewModel.getDoorStatus());
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSING, viewModel.getDoorStatusProp().get());

        model.setDoorStatus(IElevator.ELEVATOR_DOORS_OPENING);
        viewModel.update();
        assertEquals("Opening", viewModel.getDoorStatusString());
        assertEquals("Opening", viewModel.getDoorStatusStringProp().get());
        assertEquals(IElevator.ELEVATOR_DOORS_OPENING, viewModel.getDoorStatus());
        assertEquals(IElevator.ELEVATOR_DOORS_OPENING, viewModel.getDoorStatusProp().get());
    }

    @Test
    void testDirectionIntToStr() {
        model.setDirection(IElevator.ELEVATOR_DIRECTION_UP);
        viewModel.update();
        assertEquals("Up", viewModel.getDirectionString());
        assertEquals("Up", viewModel.getDirectionStringProp().get());
        assertEquals(IElevator.ELEVATOR_DIRECTION_UP, viewModel.getDirection());
        assertEquals(IElevator.ELEVATOR_DIRECTION_UP, viewModel.getDirectionProp().get());

        model.setDirection(IElevator.ELEVATOR_DIRECTION_DOWN);
        viewModel.update();
        assertEquals("Down", viewModel.getDirectionString());
        assertEquals("Down", viewModel.getDirectionStringProp().get());
        assertEquals(IElevator.ELEVATOR_DIRECTION_DOWN, viewModel.getDirection());
        assertEquals(IElevator.ELEVATOR_DIRECTION_DOWN, viewModel.getDirectionProp().get());

        model.setDirection(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);
        viewModel.update();
        assertEquals("Uncommited", viewModel.getDirectionString());
        assertEquals("Uncommited", viewModel.getDirectionStringProp().get());
        assertEquals(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED, viewModel.getDirection());
        assertEquals(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED, viewModel.getDirectionProp().get());
    }

    @Test
    void testStops() {
        viewModel.update();
        assertEquals("", viewModel.getStops());
        assertEquals("", viewModel.getStopsProp().get());

        model.setStop(0, true);
        viewModel.update();
        assertEquals("0, ", viewModel.getStops());
        assertEquals("0, ", viewModel.getStopsProp().get());

        model.setStop(1, true);
        viewModel.update();
        assertEquals("0, 1, ", viewModel.getStops());
        assertEquals("0, 1, ", viewModel.getStopsProp().get());

        model.setStop(0, false);
        viewModel.update();
        assertEquals("1, ", viewModel.getStops());
        assertEquals("1, ", viewModel.getStopsProp().get());
    }

    @Test
    void testPayloadProp() {
        model.setPayload(40);
        viewModel.update();
        assertEquals(40, viewModel.getPayload());
        assertEquals(40, viewModel.getPayloadProp().get());
    }

    @Test
    void testSpeedProp() {
        model.setSpeed(10);
        viewModel.update();
        assertEquals(10, viewModel.getSpeed());
        assertEquals(10, viewModel.getSpeedProp().get());
    }

    @Test
    void testAccelProp() {
        model.setAccel(4);
        viewModel.update();
        assertEquals(4, viewModel.getAccel());
        assertEquals(4, viewModel.getAccelProp().get());
    }

    @Test
    void testTargetProp() {
        model.setTarget(1);
        viewModel.update();
        assertEquals(1, viewModel.getTarget());
        assertEquals(1, viewModel.getTargetProp().get());
    }

    @Test
    void testDoorProp() {
        model.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        viewModel.update();
        assertEquals(IElevatorService.ELEVATOR_DOORS_OPEN, viewModel.getDoorStatus());
        assertEquals(IElevatorService.ELEVATOR_DOORS_OPEN, viewModel.getDoorStatusProp().get());
        assertEquals("Open", viewModel.getDoorStatusString());
        assertEquals("Open", viewModel.getDoorStatusStringProp().get());
    }

    @Test
    void testNearestFloorProp() {
        model.setNearestFloor(1);
        viewModel.update();
        assertEquals(1, viewModel.getNearestFloor());
        assertEquals(1, viewModel.getNearestFloorProp().get());
    }

    @Test
    void testElevatorNr() {
        viewModel.update();
        assertEquals(0, viewModel.getElevatorNr());
    }

    @Test
    void testManual() {
        assertFalse(viewModel.getManualProp().get());
    }
}
