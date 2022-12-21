/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.sqeelevator;

import at.fhhagenberg.sqelevator.Elevator;
import at.fhhagenberg.sqelevator.ElevatorException;
import at.fhhagenberg.sqelevator.Floor;
import at.fhhagenberg.sqelevator.IElevator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ElevatorTest {
    @Mock
    Floor floor1;

    @Mock
    Floor floor2;

    @Mock
    Floor floor3;

    @Mock
    IElevator api;

    ArrayList<Floor> floors;

    @BeforeEach
    void setup() {
        floors = new ArrayList<>();
        floors.add(floor1);
        floors.add(floor2);
        floors.add(floor3);
    }

    @Test
    void testObjectCreation() throws RemoteException {
        Elevator e = new Elevator(api, 0, floors);

        assertEquals(0, e.getAccel());
        assertEquals(0, e.getSpeed());
        assertEquals(0, e.getTarget());
        assertEquals(0, e.getPayload());
        assertEquals(0, e.getNearestFloor());
        assertEquals(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED, e.getDirection());
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSED, e.getDoorStatus());
    }

    @Test
    void testObjectCreationFailedNoFloors() {
        assertThrows(ElevatorException.class, () -> {
            new Elevator(api, 0, null);
        });
        assertThrows(ElevatorException.class, () -> {
            new Elevator(api, 0, new ArrayList<Floor>());
        });
    }

    @Test
    void testUpdate() throws RemoteException {
        when(api.getElevatorSpeed(0)).thenReturn(30);
        when(api.getElevatorAccel(0)).thenReturn(1);
        when(api.getTarget(0)).thenReturn(1);
        when(api.getCommittedDirection(0)).thenReturn(IElevator.ELEVATOR_DIRECTION_UP);
        when(api.getElevatorWeight(0)).thenReturn(100);
        when(api.getElevatorDoorStatus(0)).thenReturn(IElevator.ELEVATOR_DOORS_CLOSED);
        when(api.getElevatorPosition(0)).thenReturn(0);
        Elevator e = new Elevator(api, 0, floors);

        e.update();

        assertEquals(30, e.getSpeed());
        assertEquals(1, e.getAccel());
        assertEquals(1, e.getTarget());
        assertEquals(IElevator.ELEVATOR_DIRECTION_UP, e.getDirection());
        assertEquals(100, e.getPayload());
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSED, e.getDoorStatus());
        assertEquals(1, e.getNearestFloor()); //TODO(PH): review nearest floor algorithm
    }

    @Test
    void testUpdateNegativeSpeed() throws RemoteException {
        when(api.getElevatorSpeed(0)).thenReturn(-30);
        Elevator e = new Elevator(api, 0, floors);

        e.update();

        assertEquals(30, e.getSpeed());
    }

    @Test
    void testUpdateNegativeTarget() throws RemoteException {
        when(api.getElevatorSpeed(0)).thenReturn(-1);
        Elevator e = new Elevator(api, 0, floors);

        e.update();

        assertEquals(-1, e.getTarget()); //TODO(PH): underground floors ?
    }

    @Test
    void testUpdateNegativeWeight() throws RemoteException {
        when(api.getElevatorWeight(0)).thenReturn(-1);
        Elevator e = new Elevator(api, 0, floors);

        e.update();

        assertEquals(0, e.getSpeed()); //0 is  the previous value
    }

    @Test
    void testUpdateDoorStatusOutOfRange() throws RemoteException {
        when(api.getElevatorDoorStatus(0)).thenReturn(-1);
        Elevator e = new Elevator(api, 0, floors);

        e.update();

        assertEquals(IElevator.ELEVATOR_DOORS_CLOSED, e.getDoorStatus());
    }

    @Test
    void testUpdateDirectionOutOfRange() throws RemoteException {
        when(api.getCommittedDirection(0)).thenReturn(-1);
        Elevator e = new Elevator(api, 0, floors);

        e.update();

        assertEquals(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED, e.getDirection());
    }

    @Test
    void testUpdateGetElevatorSpeedFails() throws RemoteException {
        when(api.getElevatorSpeed(0)).thenThrow(RemoteException.class);
        Elevator e = new Elevator(api, 0, floors);
        assertThrows(RemoteException.class, e::update);
    }

    @Test
    void testUpdateGetElevatorAccelFails() throws RemoteException {
        when(api.getElevatorAccel(0)).thenThrow(RemoteException.class);
        Elevator e = new Elevator(api, 0, floors);
        assertThrows(RemoteException.class, e::update);
    }

    @Test
    void testUpdateGetTargetFails() throws RemoteException {
        when(api.getTarget(0)).thenThrow(RemoteException.class);
        Elevator e = new Elevator(api, 0, floors);
        assertThrows(RemoteException.class, e::update);
    }

    @Test
    void testUpdateGetCommittedDirectionFails() throws RemoteException {
        when(api.getCommittedDirection(0)).thenThrow(RemoteException.class);
        Elevator e = new Elevator(api, 0, floors);
        assertThrows(RemoteException.class, e::update);
    }

    @Test
    void testUpdateGetElevatorWeightFails() throws RemoteException {
        when(api.getElevatorWeight(0)).thenThrow(RemoteException.class);
        Elevator e = new Elevator(api, 0, floors);
        assertThrows(RemoteException.class, e::update);
    }

    @Test
    void testUpdateGetElevatorDoorStatusFails() throws RemoteException {
        when(api.getElevatorDoorStatus(0)).thenThrow(RemoteException.class);
        Elevator e = new Elevator(api, 0, floors);
        assertThrows(RemoteException.class, e::update);
    }

    @Test
    void testUpdateGetElevatorPositionFails() throws RemoteException {
        when(api.getElevatorPosition(0)).thenThrow(RemoteException.class);
        Elevator e = new Elevator(api, 0, floors);
        assertThrows(RemoteException.class, e::update);
    }
}
