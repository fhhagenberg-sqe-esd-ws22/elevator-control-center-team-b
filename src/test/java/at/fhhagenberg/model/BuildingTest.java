/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuildingTest {
    @Mock Elevator elevator1;
    @Mock Elevator elevator2;

    @Mock Floor floor1;
    @Mock Floor floor2;
    @Mock Floor floor3;

    ArrayList<Floor> floors;
    ArrayList<Elevator> elevators;

    @BeforeEach
    void setup() {
        floors = new ArrayList<>();
        floors.add(floor1);
        floors.add(floor2);
        floors.add(floor3);

        elevators = new ArrayList<>();
        elevators.add(elevator1);
        elevators.add(elevator2);
    }

    @Test
    void testObjectCreation() {
        Building b = new Building(elevators, floors);

        assertEquals(3, b.getFloors().size());
        assertEquals(2, b.getElevators().size());
    }

    @Test
    void testObjectCreationFails() {
        var emptyFloors = new ArrayList<Floor>();
        var emptyElevators =  new ArrayList<Elevator>();
        assertThrows(ModelException.class, () -> new Building(null, floors));
        assertThrows(ModelException.class, () -> new Building(elevators, null));
        assertThrows(ModelException.class, () -> new Building(emptyElevators, floors));
        assertThrows(ModelException.class, () -> new Building(elevators, emptyFloors));
    }

    @Test
    void testGetElevatorByNumber() {
        when(elevator1.getElevatorNr()).thenReturn(0);
        when(elevator2.getElevatorNr()).thenReturn(1);
        Building b = new Building(elevators, floors);

        assertEquals(0, b.getElevatorByNumber(0).getElevatorNr());
        assertEquals(1, b.getElevatorByNumber(1).getElevatorNr());
    }

    @Test
    void testGetElevatorByNumberInvalidParams() {
        when(elevator1.getElevatorNr()).thenReturn(0);
        when(elevator2.getElevatorNr()).thenReturn(1);
        Building b = new Building(elevators, floors);

        assertNull(b.getElevatorByNumber(-1));
        assertNull(b.getElevatorByNumber(2));
    }

    @Test
    void testGetFloorByNumber() {
        when(floor1.getFloorNumber()).thenReturn(0);
        when(floor2.getFloorNumber()).thenReturn(1);
        when(floor3.getFloorNumber()).thenReturn(2);
        Building b = new Building(elevators, floors);

        assertEquals(0, b.getFloorByNumber(0).getFloorNumber());
        assertEquals(1, b.getFloorByNumber(1).getFloorNumber());
        assertEquals(2, b.getFloorByNumber(2).getFloorNumber());
    }

    @Test
    void testGetFloorByNumberInvalidParams() {
        when(floor1.getFloorNumber()).thenReturn(0);
        when(floor2.getFloorNumber()).thenReturn(1);
        when(floor3.getFloorNumber()).thenReturn(2);
        Building b = new Building(elevators, floors);

        assertNull(b.getFloorByNumber(-1));
        assertNull(b.getFloorByNumber(3));
    }

}
