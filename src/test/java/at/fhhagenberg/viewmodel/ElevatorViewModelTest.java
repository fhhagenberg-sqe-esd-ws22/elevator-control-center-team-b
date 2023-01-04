package at.fhhagenberg.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.service.IElevator;
import at.fhhagenberg.viewmodels.ElevatorViewModel;

class ElevatorViewModelTest {
    static Elevator elevator;

    @BeforeEach
    void setupElevator() {
        elevator = new Elevator(0, 2);
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
        var elevatorViewModel = new ElevatorViewModel(elevator);

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
        var elevatorViewModel = new ElevatorViewModel(elevator);

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
        var elevatorViewModel = new ElevatorViewModel(elevator);
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
