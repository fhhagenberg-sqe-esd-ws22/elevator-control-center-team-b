/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sqelevator.IElevator;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RMIElevatorServiceTest {
    @Mock
    IElevator api;

    RMIElevatorService service;

    @BeforeEach
    void setup() {
        service = new RMIElevatorService(api);
    }

    @Test
    void testObjectCreationApiIsNull() {
        assertThrows(ElevatorServiceException.class, () -> { new RMIElevatorService(null); });
    }

    @Test
    void testGetCommittedDirection() throws RemoteException {
        when(api.getCommittedDirection(0)).thenReturn(IElevator.ELEVATOR_DOORS_CLOSED);
        assertEquals(IElevator.ELEVATOR_DOORS_CLOSED, service.getCommittedDirection(0));
    }

    @Test
    void testGetCommittedDirectionFails() throws RemoteException {
        when(api.getCommittedDirection(0)).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getCommittedDirection(0); });
    }

    @Test
    void testGetElevatorAccel() throws RemoteException {
        when(api.getElevatorAccel(0)).thenReturn(17);
        assertEquals(17, service.getElevatorAccel(0));
    }

    @Test
    void testGetElevatorAccelFails() throws RemoteException {
        when(api.getElevatorAccel(0)).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getElevatorAccel(0); });
    }

    @Test
    void testGetElevatorButton() throws RemoteException {
        when(api.getElevatorButton(0, 0)).thenReturn(true);
        when(api.getElevatorButton(0, 1)).thenReturn(false);
        assertTrue(service.getElevatorButton(0, 0));
        assertFalse(service.getElevatorButton(0, 1));
    }

    @Test
    void testGetElevatorButtonFails() throws RemoteException {
        when(api.getElevatorButton(0, 0)).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getElevatorButton(0, 0); });
    }

    @Test
    void testGetElevatorDoorStatus() throws RemoteException {
        when(api.getElevatorDoorStatus(0)).thenReturn(IElevator.ELEVATOR_DOORS_OPEN);
        assertEquals(IElevator.ELEVATOR_DOORS_OPEN, service.getElevatorDoorStatus(0));
    }

    @Test
    void testGetElevatorDoorStatusFails() throws RemoteException {
        when(api.getElevatorDoorStatus(0)).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getElevatorDoorStatus(0); });
    }

    @Test
    void testGetElevatorFloor() throws RemoteException {
        when(api.getElevatorFloor(0)).thenReturn(0);
        when(api.getElevatorFloor(1)).thenReturn(1);
        assertEquals(0, service.getElevatorFloor(0));
        assertEquals(1, service.getElevatorFloor(1));
    }

    @Test
    void testGetElevatorFloorFails() throws RemoteException {
        when(api.getElevatorFloor(0)).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getElevatorFloor(0); });
    }

    @Test
    void testGetElevatorNum() throws RemoteException {
        when(api.getElevatorNum()).thenReturn(17);
        assertEquals(17, service.getElevatorNum());
    }

    @Test
    void testGetElevatorNumFails() throws RemoteException {
        when(api.getElevatorNum()).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getElevatorNum(); });
    }

    @Test
    void testGetElevatorPosition() throws RemoteException {
        when(api.getElevatorPosition(0)).thenReturn(17);
        assertEquals(17, service.getElevatorPosition(0));
    }

    @Test
    void testGetElevatorPositionFails() throws RemoteException {
        when(api.getElevatorPosition(0)).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getElevatorPosition(0); });
    }

    @Test
    void testGetElevatorSpeed() throws RemoteException {
        when(api.getElevatorSpeed(0)).thenReturn(17);
        assertEquals(17, service.getElevatorSpeed(0));
    }

    @Test
    void testGetElevatorSpeedFails() throws RemoteException {
        when(api.getElevatorSpeed(0)).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getElevatorSpeed(0); });
    }

    @Test
    void testGetElevatorWeight() throws RemoteException {
        when(api.getElevatorWeight(0)).thenReturn(17);
        assertEquals(17, service.getElevatorWeight(0));
    }

    @Test
    void testGetElevatorWeightFails() throws RemoteException {
        when(api.getElevatorWeight(0)).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getElevatorWeight(0); });
    }

    @Test
    void testGetElevatorCapacity() throws RemoteException {
        when(api.getElevatorCapacity(0)).thenReturn(17);
        assertEquals(17, service.getElevatorCapacity(0));
    }

    @Test
    void testGetElevatorCapacityFails() throws RemoteException {
        when(api.getElevatorCapacity(0)).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getElevatorCapacity(0); });
    }

    @Test
    void testGetFloorButtonDown() throws RemoteException {
        when(api.getFloorButtonDown(0)).thenReturn(true);
        when(api.getFloorButtonDown(1)).thenReturn(false);
        assertTrue(service.getFloorButtonDown(0));
        assertFalse(service.getFloorButtonDown(1));
    }

    @Test
    void testGetFloorButtonDownFails() throws RemoteException {
        when(api.getFloorButtonDown(0)).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getFloorButtonDown(0); });
    }

    @Test
    void testGetFloorButtonUp() throws RemoteException {
        when(api.getFloorButtonUp(0)).thenReturn(true);
        when(api.getFloorButtonUp(1)).thenReturn(false);
        assertTrue(service.getFloorButtonUp(0));
        assertFalse(service.getFloorButtonUp(1));
    }

    @Test
    void testGetFloorButtonUpFails() throws RemoteException {
        when(api.getFloorButtonUp(0)).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getFloorButtonUp(0); });
    }

    @Test
    void testGetFloorHeight() throws RemoteException {
        when(api.getFloorHeight()).thenReturn(17);
        assertEquals(17, service.getFloorHeight());
    }

    @Test
    void testGetFloorHeightFails() throws RemoteException {
        when(api.getFloorHeight()).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getFloorHeight(); });
    }

    @Test
    void testGetFloorNum() throws RemoteException {
        when(api.getFloorNum()).thenReturn(17);
        assertEquals(17, service.getFloorNum());
    }

    @Test
    void testGetFloorNumFails() throws RemoteException {
        when(api.getFloorNum()).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getFloorNum(); });
    }

    @Test
    void testGetServicesFloors() throws RemoteException {
        when(api.getServicesFloors(0, 0)).thenReturn(true);
        when(api.getServicesFloors(0, 1)).thenReturn(false);
        assertTrue(service.getServicesFloors(0, 0));
        assertFalse(service.getServicesFloors(0, 1));
    }

    @Test
    void testGetServicesFloorsFails() throws RemoteException {
        when(api.getServicesFloors(0, 0)).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getServicesFloors(0, 0); });
    }

    @Test
    void testGetTarget() throws RemoteException {
        when(api.getTarget(0)).thenReturn(17);
        assertEquals(17, service.getTarget(0));
    }

    @Test
    void testGetTargetFails() throws RemoteException {
        when(api.getTarget(0)).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getTarget(0); });
    }

    @Test
    void testSetCommittedDirection() throws RemoteException {
        service.setCommittedDirection(0, IElevatorService.ELEVATOR_DIRECTION_DOWN);
        verify(api).setCommittedDirection(0, IElevatorService.ELEVATOR_DIRECTION_DOWN);
    }

    @Test
    void testSetCommittedDirectionFails() throws RemoteException {
        doThrow(RemoteException.class).when(api).setCommittedDirection(0, IElevatorService.ELEVATOR_DIRECTION_DOWN);
        assertThrows(ElevatorServiceException.class, () -> { service.setCommittedDirection(0, IElevatorService.ELEVATOR_DIRECTION_DOWN); });
    }

    @Test
    void testSetServicesFloors() throws RemoteException {
        service.setServicesFloors(0, 0, true);
        verify(api).setServicesFloors(0, 0, true);
    }

    @Test
    void testSetServicesFloorsFails() throws RemoteException {
        doThrow(RemoteException.class).when(api).setServicesFloors(0, 0, true);
        assertThrows(ElevatorServiceException.class, () -> { service.setServicesFloors(0, 0, true); });
    }

    @Test
    void testSetTarget() throws RemoteException {
        service.setTarget(0, 1);
        verify(api).setTarget(0, 1);
    }

    @Test
    void testSetTargetFails() throws RemoteException {
        doThrow(RemoteException.class).when(api).setTarget(0, 1);
        assertThrows(ElevatorServiceException.class, () -> { service.setTarget(0, 1); });
    }

    @Test
    void testGetClockTick() throws RemoteException {
        when(api.getClockTick()).thenReturn(17L);
        assertEquals(17, service.getClockTick());
    }

    @Test
    void testGetClockTickFails() throws RemoteException {
        when(api.getClockTick()).thenThrow(RemoteException.class);
        assertThrows(ElevatorServiceException.class, () -> { service.getClockTick(); });
    }
}
