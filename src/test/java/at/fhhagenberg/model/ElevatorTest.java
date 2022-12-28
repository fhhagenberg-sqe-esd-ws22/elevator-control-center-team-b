package at.fhhagenberg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import at.fhhagenberg.service.IElevator;

class ElevatorTest {
    @Test
    void testSetValidPositiveSpeed() {
        Elevator elevator = new Elevator(0, 1);
        elevator.setSpeed(8);
        assertEquals(8, elevator.getSpeed());
    }

    @Test
    void testSetValidZeroSpeed() {
        Elevator elevator = new Elevator(0, 1);
        elevator.setSpeed(5);
        assertEquals(5, elevator.getSpeed());

        elevator.setSpeed(0);
        assertEquals(0, elevator.getSpeed());
    }

    @Test
    void testNegativeSpeed() {
        Elevator elevator = new Elevator(0, 1);
        elevator.setSpeed(5);
        assertEquals(5, elevator.getSpeed());

        elevator.setSpeed(-1);
        assertEquals(1, elevator.getSpeed());
    }

    @Test
    void testChangeToNegativeTargetFloor() {
        Elevator elevator = new Elevator(0, 1);
        elevator.setTarget(0);
        assertEquals(0, elevator.getTarget());

        elevator.setTarget(-1);
        assertEquals(0, elevator.getTarget());
    }

    @Test
    void testChangeToInvalidTargetFloor() {
        Elevator elevator = new Elevator(0, 1);
        elevator.setTarget(0);
        assertEquals(0, elevator.getTarget());

        elevator.setTarget(3);
        assertEquals(0, elevator.getTarget());
    }

    @Test
    void testChangeToInvalidDoorStatus() {
        Elevator elevator = new Elevator(0, 1);
        elevator.setDoorStatus(IElevator.ELEVATOR_DOORS_CLOSED);
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSED, elevator.getDoorStatus());

        elevator.setDoorStatus(IElevator.ELEVATOR_DOORS_OPEN - 1);
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSED, elevator.getDoorStatus());
        elevator.setDoorStatus(IElevator.ELEVATOR_DOORS_CLOSING + 1);
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSED, elevator.getDoorStatus());
    }

    @Test
    void testChangeToInvalidDirection() {
        Elevator elevator = new Elevator(0, 1);
        elevator.setDirection(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);
        assertEquals(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED, elevator.getDirection());

        elevator.setDirection(IElevator.ELEVATOR_DIRECTION_UP - 1);
        assertEquals(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED, elevator.getDirection());
        elevator.setDirection(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED + 1);
        assertEquals(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED, elevator.getDirection());
    }

    @Test
    void testValidPayload() {
        Elevator elevator = new Elevator(0, 1);
        elevator.setPayload(0);
        assertEquals(0, elevator.getPayload());

        elevator.setPayload(500000);
        assertEquals(500000, elevator.getPayload());
    }

    @Test
    void testInvalidPayload() {
        Elevator elevator = new Elevator(0, 1);
        elevator.setPayload(0);
        assertEquals(0, elevator.getPayload());
        
        elevator.setPayload(-1);
        assertEquals(0, elevator.getPayload());
    }

    @Test
    void testInvalidNearestFloor() {
        Elevator elevator = new Elevator(0, 1);
        elevator.setNearestFloor(0);
        assertEquals(0, elevator.getNearestFloor());

        elevator.setNearestFloor(-1);
        assertEquals(0, elevator.getNearestFloor());
        elevator.setNearestFloor(1);
        assertEquals(0, elevator.getNearestFloor());
    }
}
