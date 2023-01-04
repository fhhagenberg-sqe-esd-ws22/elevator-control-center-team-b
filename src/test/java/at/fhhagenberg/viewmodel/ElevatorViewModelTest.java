package at.fhhagenberg.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import at.fhhagenberg.logic.BusinesLogic;
import at.fhhagenberg.mock_observable.MockElevatorService;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.model.ModelFactory;
import at.fhhagenberg.service.IElevator;
import at.fhhagenberg.viewmodels.ElevatorViewModel;

class ElevatorViewModelTest {
    static Elevator elevator;
    static ElevatorViewModel elevatorViewModel;

    @BeforeAll
    static void setup() {
        MockElevatorService service = new MockElevatorService(1, 2, 10);
        ModelFactory factory = new ModelFactory(service);
        Building building = factory.createBuilding();
        elevator = building.getElevatorByNumber(0);
        BusinesLogic logic = new BusinesLogic(building);
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

        elevator.setDoorStatus(IElevator.ELEVATOR_DOORS_OPEN);
        elevatorViewModel.update();
        assertEquals("Open", elevatorViewModel.getDoorStatusString());

        elevator.setDoorStatus(IElevator.ELEVATOR_DOORS_CLOSING);
        elevatorViewModel.update();
        assertEquals("Closing", elevatorViewModel.getDoorStatusString());

        elevator.setDoorStatus(IElevator.ELEVATOR_DOORS_OPENING);
        elevatorViewModel.update();
        assertEquals("Opening", elevatorViewModel.getDoorStatusString());
    }

    @Test
    void testDirectionIntToStr() {
        elevator.setDirection(IElevator.ELEVATOR_DIRECTION_UP);
        elevatorViewModel.update();
        assertEquals("Up", elevatorViewModel.getDirectionString());

        elevator.setDirection(IElevator.ELEVATOR_DIRECTION_DOWN);
        elevatorViewModel.update();
        assertEquals("Down", elevatorViewModel.getDirectionString());

        elevator.setDirection(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);
        elevatorViewModel.update();
        assertEquals("Uncommited", elevatorViewModel.getDirectionString());
    }

    @Test
    void testStops() {
        elevatorViewModel.update();
        assertEquals("", elevatorViewModel.getStops());

        elevator.setStop(0, true);
        elevatorViewModel.update();
        assertEquals("0, ", elevatorViewModel.getStops());

        elevator.setStop(1, true);
        elevatorViewModel.update();
        assertEquals("0, 1, ", elevatorViewModel.getStops());

        elevator.setStop(0, false);
        elevatorViewModel.update();
        assertEquals("1, ", elevatorViewModel.getStops());
    }
}
