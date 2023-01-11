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
class ElevatorUpdaterTest {

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
        when(elevator.getTarget()).thenReturn(0);
        when(elevator.getDirection()).thenReturn(IElevatorService.ELEVATOR_DIRECTION_UNCOMMITTED);

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

    // these calls are all made in update() and expected to bring correct results,
    // but they are unnecessary to see if serviced floors and stops were correctly handled
    void setIrrelevantUpdateCalls(){
        when(service.getTarget(0)).thenReturn(1);
        when(service.getFloorNum()).thenReturn(3);
        when(service.getFloorHeight()).thenReturn(10);
        when(service.getElevatorAccel(0)).thenReturn(1);
        when(service.getCommittedDirection(0)).thenReturn(IElevatorService.ELEVATOR_DIRECTION_UP);
        when(service.getElevatorWeight(0)).thenReturn(100);
        when(service.getElevatorDoorStatus(0)).thenReturn(IElevatorService.ELEVATOR_DOORS_CLOSED);
    }

    @Test
    void testTargetDirectionUpdate(){
        when(service.getFloorHeight()).thenReturn(10); // otherwise there is an exception because of a division through 0
        when(elevator.getElevatorNr()).thenReturn(0);
        when(elevator.getTarget()).thenReturn(3);
        when(elevator.getDirection()).thenReturn(IElevatorService.ELEVATOR_DIRECTION_DOWN);
        when(service.getTarget(0)).thenReturn(1);
        when(service.getCommittedDirection(0)).thenReturn(IElevatorService.ELEVATOR_DIRECTION_UP);

        ElevatorUpdater updater = new ElevatorUpdater(service, elevator);
        updater.update();

        verify(service).setTarget(0,3);
        verify(service).setCommittedDirection(0,IElevatorService.ELEVATOR_DIRECTION_DOWN);
    }

    @Test
    void testRequestStops() {
        when(elevator.getElevatorNr()).thenReturn(0);
        
       setIrrelevantUpdateCalls();

        when(service.getElevatorButton(0, 0)).thenReturn(true);
        when(service.getElevatorButton(0, 1)).thenReturn(false);
        when(service.getElevatorButton(0, 2)).thenReturn(true);

        ElevatorUpdater updater = new ElevatorUpdater(service, elevator);
        updater.update();

        verify(elevator).setStop(0, true);
        verify(elevator).setStop(1, false);
        verify(elevator).setStop(2, true);
    }

    @Test
    void testServiced() {
        when(elevator.getElevatorNr()).thenReturn(0);
        when(service.getTarget(0)).thenReturn(1);

        setIrrelevantUpdateCalls();

        when(service.getServicesFloors(0, 0)).thenReturn(true);
        when(service.getServicesFloors(0, 1)).thenReturn(false);
        when(service.getServicesFloors(0, 2)).thenReturn(true);

        ElevatorUpdater updater = new ElevatorUpdater(service, elevator);
        updater.update();

        verify(elevator).setServiced(0, true);
        verify(elevator).setServiced(1, false);
        verify(elevator).setServiced(2, true);
    }

    private static Stream<Arguments> provideHeights() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(10, 1),
                Arguments.of(20, 2),
                Arguments.of(15, 2),
                Arguments.of(14, 1),
                Arguments.of(5, 1),
                Arguments.of(100, 2),
                Arguments.of(30, 2),
                Arguments.of(35, 2),
                Arguments.of(4, 0)
        );
    }
}
