/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.ModelFactory;
import at.fhhagenberg.service.IElevatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdaterFactoryTest {
    @Mock
    IElevatorService service;

    Building building;

    @BeforeEach
    void setup() {
        when(service.getFloorNum()).thenReturn(3);
        when(service.getElevatorNum()).thenReturn(2);
        ModelFactory factory = new ModelFactory(service);
        building = factory.createBuilding();
    }

    @Test
    void testObjectCreationServiceIsNull() {
        assertThrows(UpdaterException.class, () -> { new UpdaterFactory(null); });
    }

    @Test
    void testCreateBuildingUpdater() {
        UpdaterFactory updaterFactory = new UpdaterFactory(service);

        BuildingUpdater updater = updaterFactory.createBuildingUpdater(building);
        assertNotNull(updater);
    }
}
