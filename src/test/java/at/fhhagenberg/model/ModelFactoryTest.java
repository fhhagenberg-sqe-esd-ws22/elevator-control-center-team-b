/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.model;

import at.fhhagenberg.service.ElevatorServiceException;
import at.fhhagenberg.service.IElevatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModelFactoryTest {
    @Mock
    IElevatorService service;

    @Test
    void testCreateBuilding() {
        when(service.getFloorNum()).thenReturn(3);
        when(service.getElevatorNum()).thenReturn(2);
        ModelFactory factory = new ModelFactory(service);

        Building b = factory.createBuilding();

        assertEquals(3, b.getFloors().size());
        assertEquals(2, b.getElevators().size());

        assertEquals(0, b.getElevators().get(0).getElevatorNr());
        assertEquals(1, b.getElevators().get(1).getElevatorNr());

        assertEquals(0, b.getFloors().get(0).getFloorNumber());
        assertEquals(1, b.getFloors().get(1).getFloorNumber());
        assertEquals(2, b.getFloors().get(2).getFloorNumber());
    }

    @Test
    void testObjectCreationFails() {
        assertThrows(ModelException.class, () -> { new ModelFactory(null); });
    }

    @Test
    void testCreateBuildingGetFloorNumFails() {
        when(service.getFloorNum()).thenThrow(ElevatorServiceException.class);
        ModelFactory factory = new ModelFactory(service);

        assertThrows(ModelException.class, factory::createBuilding);
    }

    @Test
    void testCreateBuildingGetElevatorNumFails() {
        when(service.getFloorNum()).thenReturn(3);
        when(service.getElevatorNum()).thenThrow(ElevatorServiceException.class);
        ModelFactory factory = new ModelFactory(service);

        assertThrows(ModelException.class, factory::createBuilding);
    }

}
