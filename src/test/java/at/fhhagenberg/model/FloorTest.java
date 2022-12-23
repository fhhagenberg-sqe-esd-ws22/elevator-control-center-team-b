/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
/*
package at.fhhagenberg.model;


import at.fhhagenberg.service.IElevator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FloorTest {

    @Mock
    IElevator api;

    @Test
    void testObjectCreation() {
        Floor f = new Floor(api, 0, 0);
        assertEquals(0, f.getElevatorNumber());
        assertEquals(0, f.getFloorNumber());
        assertFalse(f.getWantDown());
        assertFalse(f.getWantUp());
        assertFalse(f.getElevatorButtonPressed());
        assertTrue(f.isServiced());
    }

    @Test
    void testUpdateIsServiced() throws RemoteException {
        Floor f = new Floor(api, 0, 0);

        when(api.getServicesFloors(0,0)).thenReturn(true);
        when(api.getFloorButtonUp(0)).thenReturn(true);
        when(api.getFloorButtonDown(0)).thenReturn(true);
        when(api.getElevatorButton(0, 0)).thenReturn(true);

        f.update();

        verify(api).getServicesFloors(0, 0);
        verify(api).getFloorButtonUp(0);
        verify(api).getFloorButtonDown(0);
        verify(api).getElevatorButton(0, 0);

        assertTrue(f.getWantUp());
        assertTrue(f.getWantDown());
        assertTrue(f.getElevatorButtonPressed());
    }

    @Test
    void testUpdateNotServiced() throws RemoteException {
        Floor f = new Floor(api, 0, 0);
        when(api.getServicesFloors(0,0)).thenReturn(false);

        f.update();

        verify(api).getServicesFloors(0, 0);

        assertFalse(f.getWantUp());
        assertFalse(f.getWantDown());
        assertFalse(f.getElevatorButtonPressed());
    }

    @Test
    void testUpdateGetServiceFloorsFails() throws RemoteException {
        Floor f = new Floor(api, 0, 0);

        when(api.getServicesFloors(0,0)).thenReturn(true);
        when(api.getServicesFloors(0, 0)).thenThrow(RemoteException.class);
        assertThrows(RemoteException.class, f::update);
    }

    @Test
    void testUpdateGetFloorButtonUpFails() throws RemoteException {
        Floor f = new Floor(api, 0, 0);

        when(api.getServicesFloors(0,0)).thenReturn(true);
        when(api.getFloorButtonUp(0)).thenThrow(RemoteException.class);
        assertThrows(RemoteException.class, f::update);
    }

    @Test
    void testUpdateGetFloorButtonDownFails() throws RemoteException {
        Floor f = new Floor(api, 0, 0);

        when(api.getServicesFloors(0,0)).thenReturn(true);
        when(api.getFloorButtonDown(0)).thenThrow(RemoteException.class);
        assertThrows(RemoteException.class, f::update);
    }

    @Test
    void testUpdateGetElevatorButtonFails() throws RemoteException {
        Floor f = new Floor(api, 0, 0);

        when(api.getServicesFloors(0,0)).thenReturn(true);
        when(api.getElevatorButton(0, 0)).thenThrow(RemoteException.class);
        assertThrows(RemoteException.class, f::update);

    }
}
*/