package at.fhhagenberg.model;

import org.junit.jupiter.api.Test;
import sqelevator.IElevator;

import static org.junit.jupiter.api.Assertions.*;

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
    void testInvalidFloor() {
        Elevator elevator = new Elevator(0, 1);
        elevator.setFloor(0);
        assertEquals(0, elevator.getFloor());

        elevator.setFloor(-1);
        assertEquals(0, elevator.getFloor());
        elevator.setFloor(1);
        assertEquals(0, elevator.getFloor());
    }

    @Test
    void testSetAndResetStops() {
        Elevator elevator = new Elevator(0, 2);
        assertFalse(elevator.getStop(0));
        assertFalse(elevator.getStop(1));

        // set elevator to stop at both floors
        elevator.setStop(0, true);
        assertTrue(elevator.getStop(0));
        elevator.setStop(1, true);
        assertTrue(elevator.getStop(1));

        // set elevator to stop at no floor
        elevator.setStop(0, false);
        assertFalse(elevator.getStop(0));
        elevator.setStop(1, false);
        assertFalse(elevator.getStop(1));
    }

    @Test
    void testSetAndResetServiced() {
        Elevator elevator = new Elevator(0, 2);
        assertTrue(elevator.getServiced(0));

        elevator.setServiced(0, false);
        assertFalse(elevator.getServiced(0));

        elevator.setServiced(1, true);
        assertTrue(elevator.getServiced(1));
    }

    @Test
    void testInvalidFloorStops() {
        Elevator elevator = new Elevator(0, 1);
        assertFalse(elevator.getStop(0));

        elevator.setStop(0, true);
        assertTrue(elevator.getStop(0));

        elevator.setStop(-1, false);
        assertThrows(ModelException.class, () -> elevator.getStop(-1));
        assertTrue(elevator.getStop(0));
        elevator.setStop(1, false);
        assertThrows(ModelException.class, () -> elevator.getStop(1));
        assertTrue(elevator.getStop(0));
    }

    @Test
    void testInvalidFloorServiced() {
        Elevator elevator = new Elevator(0, 1);
        elevator.setServiced(0, true);
        assertTrue(elevator.getServiced(0));

        elevator.setServiced(-1, false);
        assertThrows(ModelException.class, () -> elevator.getServiced(-1));
        assertTrue(elevator.getServiced(0));
        elevator.setServiced(1, false);
        assertThrows(ModelException.class, () -> elevator.getServiced(1));
        assertTrue(elevator.getServiced(0));
    }
}
