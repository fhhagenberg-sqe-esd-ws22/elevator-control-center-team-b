/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

import at.fhhagenberg.model.Floor;
import at.fhhagenberg.service.ElevatorServiceException;
import at.fhhagenberg.service.IElevatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FloorUpdaterTest {
    @Mock
    IElevatorService service;

    @Mock
    Floor floor;

    @Test
    void testObjectCreationServiceIsNull() {
        assertThrows(UpdaterException.class, () -> { new FloorUpdater(null, floor); });
    }

    @Test
    void testObjectCreationModelIsNull() {
        assertThrows(UpdaterException.class, () -> { new FloorUpdater(service, null); });
    }

    @Test
    void testUpdate() {
        when(floor.getFloorNumber()).thenReturn(0);
        when(service.getFloorButtonUp(0)).thenReturn(true); //per default false
        when(service.getFloorButtonDown(0)).thenReturn(true);

        FloorUpdater updater = new FloorUpdater(service, floor);
        updater.update();

        verify(floor).setWantUp(true);
        verify(floor).setWantDown(true);
    }

    @Test
    void testUpdateGetFloorButtonUpFails() {
        when(floor.getFloorNumber()).thenReturn(0);
        when(service.getFloorButtonUp(0)).thenThrow(ElevatorServiceException.class); //per default false

        FloorUpdater updater = new FloorUpdater(service, floor);

        assertThrows(ElevatorServiceException.class, updater::update);
    }

    @Test
    void testUpdateGetFloorButtonDownFails() {
        when(floor.getFloorNumber()).thenReturn(0);
        when(service.getFloorButtonUp(0)).thenReturn(true); //per default false
        when(service.getFloorButtonDown(0)).thenThrow(ElevatorServiceException.class);

        FloorUpdater updater = new FloorUpdater(service, floor);

        assertThrows(ElevatorServiceException.class, updater::update);
    }
}
