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
    static Elevator elevator;
    static ElevatorViewModel elevatorViewModel;
    static BusinesLogic logic;

    @BeforeAll
    static void setup() {
        MockElevatorService service = new MockElevatorService(1, 2, 10);
        ModelFactory factory = new ModelFactory(service);
        Building building = factory.createBuilding();
        elevator = building.getElevatorByNumber(0);
        logic = new BusinesLogic(building);
        elevatorViewModel = new ElevatorViewModel(elevator, logic);
    }

    @BeforeEach
    void setupElevator() {
        elevator.setAccel(0);
        elevator.setDirection(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);
        elevator.setDoorStatus(IElevator.ELEVATOR_DOORS_CLOSED);
        elevator.setNearestFloor(0);
        elevator.setPayload(0);
        elevator.setSpeed(0);
        elevator.setTarget(0);
    }

    @Test
    void testDoorStatusIntToStr() {
        elevator.setDoorStatus(IElevator.ELEVATOR_DOORS_CLOSED);
        elevatorViewModel.update();
        assertEquals("Closed", elevatorViewModel.getDoorStatusString());
        assertEquals("Closed", elevatorViewModel.getDoorStatusStringProp().get());
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSED, elevatorViewModel.getDoorStatus());
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSED, elevatorViewModel.getDoorStatusProp().get());

        elevator.setDoorStatus(IElevator.ELEVATOR_DOORS_OPEN);
        elevatorViewModel.update();
        assertEquals("Open", elevatorViewModel.getDoorStatusString());
        assertEquals("Open", elevatorViewModel.getDoorStatusStringProp().get());
        assertEquals(IElevator.ELEVATOR_DOORS_OPEN, elevatorViewModel.getDoorStatus());
        assertEquals(IElevator.ELEVATOR_DOORS_OPEN, elevatorViewModel.getDoorStatusProp().get());

        elevator.setDoorStatus(IElevator.ELEVATOR_DOORS_CLOSING);
        elevatorViewModel.update();
        assertEquals("Closing", elevatorViewModel.getDoorStatusString());
        assertEquals("Closing", elevatorViewModel.getDoorStatusStringProp().get());
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSING, elevatorViewModel.getDoorStatus());
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSING, elevatorViewModel.getDoorStatusProp().get());

        elevator.setDoorStatus(IElevator.ELEVATOR_DOORS_OPENING);
        elevatorViewModel.update();
        assertEquals("Opening", elevatorViewModel.getDoorStatusString());
        assertEquals("Opening", elevatorViewModel.getDoorStatusStringProp().get());
        assertEquals(IElevator.ELEVATOR_DOORS_OPENING, elevatorViewModel.getDoorStatus());
        assertEquals(IElevator.ELEVATOR_DOORS_OPENING, elevatorViewModel.getDoorStatusProp().get());
    }

    @Test
    void testDirectionIntToStr() {
        elevator.setDirection(IElevator.ELEVATOR_DIRECTION_UP);
        elevatorViewModel.update();
        assertEquals("Up", elevatorViewModel.getDirectionString());
        assertEquals("Up", elevatorViewModel.getDirectionStringProp().get());
        assertEquals(IElevator.ELEVATOR_DIRECTION_UP, elevatorViewModel.getDirection());
        assertEquals(IElevator.ELEVATOR_DIRECTION_UP, elevatorViewModel.getDirectionProp().get());

        elevator.setDirection(IElevator.ELEVATOR_DIRECTION_DOWN);
        elevatorViewModel.update();
        assertEquals("Down", elevatorViewModel.getDirectionString());
        assertEquals("Down", elevatorViewModel.getDirectionStringProp().get());
        assertEquals(IElevator.ELEVATOR_DIRECTION_DOWN, elevatorViewModel.getDirection());
        assertEquals(IElevator.ELEVATOR_DIRECTION_DOWN, elevatorViewModel.getDirectionProp().get());

        elevator.setDirection(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);
        elevatorViewModel.update();
        assertEquals("Uncommited", elevatorViewModel.getDirectionString());
        assertEquals("Uncommited", elevatorViewModel.getDirectionStringProp().get());
        assertEquals(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED, elevatorViewModel.getDirection());
        assertEquals(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED, elevatorViewModel.getDirectionProp().get());
    }

    @Test
    void testStops() {
        elevatorViewModel.update();
        assertEquals("", elevatorViewModel.getStops());
        assertEquals("", elevatorViewModel.getStopsProp().get());

        elevator.setStop(0, true);
        elevatorViewModel.update();
        assertEquals("0, ", elevatorViewModel.getStops());
        assertEquals("0, ", elevatorViewModel.getStopsProp().get());

        elevator.setStop(1, true);
        elevatorViewModel.update();
        assertEquals("0, 1, ", elevatorViewModel.getStops());
        assertEquals("0, 1, ", elevatorViewModel.getStopsProp().get());

        elevator.setStop(0, false);
        elevatorViewModel.update();
        assertEquals("1, ", elevatorViewModel.getStops());
        assertEquals("1, ", elevatorViewModel.getStopsProp().get());
    }

    @Test
    void testPayloadProp() {
        elevator.setPayload(40);
        elevatorViewModel.update();
        assertEquals(40, elevatorViewModel.getPayload());
        assertEquals(40, elevatorViewModel.getPayloadProp().get());
    }

    @Test
    void testSpeedProp() {
        elevator.setSpeed(10);
        elevatorViewModel.update();
        assertEquals(10, elevatorViewModel.getSpeed());
        assertEquals(10, elevatorViewModel.getSpeedProp().get());
    }

    @Test
    void testAccelProp() {
        elevator.setAccel(4);
        elevatorViewModel.update();
        assertEquals(4, elevatorViewModel.getAccel());
        assertEquals(4, elevatorViewModel.getAccelProp().get());
    }

    @Test
    void testTargetProp() {
        elevator.setTarget(1);
        elevatorViewModel.update();
        assertEquals(1, elevatorViewModel.getTarget());
        assertEquals(1, elevatorViewModel.getTargetProp().get());
    }

    @Test
    void testDoorProp() {
        elevator.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        elevatorViewModel.update();
        assertEquals(IElevatorService.ELEVATOR_DOORS_OPEN, elevatorViewModel.getDoorStatus());
        assertEquals(IElevatorService.ELEVATOR_DOORS_OPEN, elevatorViewModel.getDoorStatusProp().get());
        assertEquals("Open", elevatorViewModel.getDoorStatusString());
        assertEquals("Open", elevatorViewModel.getDoorStatusStringProp().get());
    }

    @Test
    void testNearestFloorProp() {
        elevator.setNearestFloor(1);
        elevatorViewModel.update();
        assertEquals(1, elevatorViewModel.getNearestFloor());
        assertEquals(1, elevatorViewModel.getNearestFloorProp().get());
    }

    @Test
    void testElevatorNr() {
        elevatorViewModel.update();
        assertEquals(0, elevatorViewModel.getElevatorNr());
    }

    @Test
    void testManual() {
        assertFalse(elevatorViewModel.getManualProp().get());
    }
}
