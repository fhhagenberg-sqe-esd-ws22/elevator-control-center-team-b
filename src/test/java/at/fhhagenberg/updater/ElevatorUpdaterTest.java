/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.service.IElevatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ElevatorUpdaterTest {

    @Mock
    IElevatorService service;

    @Mock
    Elevator elevator;

    @Test
    void testObjectCreationElevatorSerivceIsNull() {
        assertThrows(UpdaterException.class, () -> { new ElevatorUpdater(null, elevator); });
    }

    @Test
    void testObjectCreationModelIsNull() {
        assertThrows(UpdaterException.class, () -> { new ElevatorUpdater(service, null); });
    }

    @ParameterizedTest
    @MethodSource("provideHeights")
    void testUpdateNearestFloorIsMinimum(int elevatorPos, int nearestFloor) {
        when(elevator.getElevatorNr()).thenReturn(0);
        when(elevator.getNrOfFloors()).thenReturn(3);

        when(service.getElevatorSpeed(0)).thenReturn(30);
        when(service.getElevatorAccel(0)).thenReturn(1);
        when(service.getTarget(0)).thenReturn(1);
        when(service.getCommittedDirection(0)).thenReturn(IElevatorService.ELEVATOR_DIRECTION_UP);
        when(service.getElevatorWeight(0)).thenReturn(100);
        when(service.getElevatorDoorStatus(0)).thenReturn(IElevatorService.ELEVATOR_DOORS_CLOSED);
        when(service.getElevatorPosition(0)).thenReturn(elevatorPos);
        when(service.getFloorHeight()).thenReturn(10);

        ElevatorUpdater updater = new ElevatorUpdater(service, elevator);
        updater.update();

        verify(elevator).setSpeed(30);
        verify(elevator).setAccel(1);
        verify(elevator).setTarget(1);
        verify(elevator).setDirection(IElevatorService.ELEVATOR_DIRECTION_UP);
        verify(elevator).setPayload(100);
        verify(elevator).setDoorStatus(IElevatorService.ELEVATOR_DOORS_CLOSED);
        verify(elevator).setNearestFloor(nearestFloor);
    }

    private static Stream<Arguments> provideHeights() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(10, 1),
                Arguments.of(20, 2),
                Arguments.of(15, 2),
                Arguments.of(14, 1),
                Arguments.of(5, 1),
                Arguments.of(4, 0)
        );
    }
}
