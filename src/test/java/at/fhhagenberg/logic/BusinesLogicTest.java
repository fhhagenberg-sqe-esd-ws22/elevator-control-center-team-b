package at.fhhagenberg.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import at.fhhagenberg.mock_observable.MockElevatorService;
import at.fhhagenberg.model.ModelFactory;

class BusinesLogicTest {
    @Test
    void testSetGetManual() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(2, 2, 10));
        BusinesLogic logic = new BusinesLogic(factory.createBuilding());
        assertFalse(logic.getManual(0));
        assertFalse(logic.getManual(1));

        logic.setManual(0, true);
        assertTrue(logic.getManual(0));
        assertFalse(logic.getManual(1));
        
        logic.setManual(1, true);
        assertTrue(logic.getManual(0));
        assertTrue(logic.getManual(1));
    }    
}
