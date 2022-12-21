/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.sqeelevator;

import at.fhhagenberg.sqelevator.Building;
import at.fhhagenberg.sqelevator.ElevatorException;
import at.fhhagenberg.sqelevator.IElevator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuildingTest {
    @Mock
    IElevator api;

    @Test
    void testObjectCreation() throws RemoteException {
        when(api.getElevatorNum()).thenReturn(3);
        when(api.getFloorNum()).thenReturn(4);

        Building b = new Building(api);

        assertEquals(3, b.getElevators().size());
        assertEquals(4, b.getElevators().get(0).getFloors().size());
    }

    @Test
    void testObjectCreationFails() throws RemoteException {
        when(api.getElevatorNum()).thenThrow(RemoteException.class);
        assertThrows(ElevatorException.class, () -> { new Building(api); });
    }

    @Test
    void testUpdate() throws RemoteException {
        when(api.getElevatorNum()).thenReturn(3);
        when(api.getFloorNum()).thenReturn(4);

        Building b = new Building(api);
        b.update(); //TODO(PH): maybe assert something
    }

    @Test
    void testUpdateFails() throws RemoteException {
        when(api.getElevatorNum()).thenReturn(3);
        when(api.getFloorNum()).thenReturn(4);
        when(api.getElevatorSpeed(0)).thenThrow(RemoteException.class);
        Building b = new Building(api);

        assertThrows(ElevatorException.class, b::update);
    }
}
