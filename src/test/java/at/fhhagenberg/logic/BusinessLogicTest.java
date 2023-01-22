package at.fhhagenberg.logic;

import at.fhhagenberg.mock_observable.MockElevatorService;
import at.fhhagenberg.model.ModelFactory;
import at.fhhagenberg.service.IElevatorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessLogicTest {
    @Test
    void testObjectConstruction() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(2, 2, 10));
        BusinessLogic logic = new BusinessLogic(factory.createBuilding());

        for (int i = 0; i < 2; i++) {
            assertFalse(logic.getManual(i));
        }
    }

    @Test
    void testSetGetManual() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(2, 2, 10));
        BusinessLogic logic = new BusinessLogic(factory.createBuilding());
        assertFalse(logic.getManual(0));
        assertFalse(logic.getManual(1));

        logic.setManual(0, true);
        assertTrue(logic.getManual(0));
        assertFalse(logic.getManual(1));

        logic.setManual(1, true);
        assertTrue(logic.getManual(0));
        assertTrue(logic.getManual(1));
    }

    @Test
    void testSetManualTargetStaysTheSame() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(1, 2, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        elevator0.setTarget(1);

        logic.setManual(0, true);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UNCOMMITTED, elevator0.getDirection());
    }

    @Test
    void testSetElevatorManualTarget() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(1, 2, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);

        elevator0.setStop(1, true);
        logic.setNextTargets();

        logic.setManual(0, true);
        logic.setElevatorManualTarget(0, 0);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());

        elevator0.setFloor(1); //reached original target floor
        logic.setElevatorManualTarget(0, 0);
        logic.setNextTargets();
        assertEquals(0, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_DOWN, elevator0.getDirection());
    }

    @Test
    void testManualModeWithUnservicedFloor() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(1, 2, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        elevator0.setServiced(1, false);
        elevator0.setTarget(1);

        logic.setManual(0, true);
        logic.setElevatorManualTarget(0, 0);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());

        elevator0.setFloor(1);
        logic.setNextTargets();
        assertEquals(0, elevator0.getTarget());
    }

    @Test
    void testAutomaticModeSingleElevatorPressedInsideSequence() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(1, 4, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);

        elevator0.setStop(1, true);
        elevator0.setStop(3, true);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());

        elevator0.setFloor(1);
        elevator0.setStop(2, true);
        logic.setNextTargets();
        assertEquals(2, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
        elevator0.setStop(1, false);

        elevator0.setFloor(2);
        elevator0.setStop(1, true);
        logic.setNextTargets();
        assertEquals(3, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
        elevator0.setStop(2, false);

        elevator0.setFloor(3);
        elevator0.setStop(2, true);
        logic.setNextTargets();
        assertEquals(2, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_DOWN, elevator0.getDirection());
        elevator0.setStop(3, false);

        elevator0.setFloor(2);
        elevator0.setStop(3, true);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_DOWN, elevator0.getDirection());
        elevator0.setStop(2, false);

        elevator0.setFloor(1);
        logic.setNextTargets();
        assertEquals(3, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
    }

    @Test
    void testAutomaticModeSingleElevatorPressedOutsideSequence() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(1, 4, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var floors = building.getFloors();

        floors.get(1).setWantUp(true);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());

        elevator0.setFloor(1);
        floors.get(2).setWantDown(true);
        floors.get(3).setWantUp(true);
        logic.setNextTargets();
        assertEquals(3, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());

        elevator0.setFloor(3);
        logic.setNextTargets();
        assertEquals(2, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_DOWN, elevator0.getDirection());
        floors.get(3).setWantUp(false);

        elevator0.setFloor(2);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_DOWN, elevator0.getDirection());
    }

    @Test
    void testAutomaticModeSingleElevatorPressedInsideAndOutsideSequence() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(1, 4, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var floors = building.getFloors();

        elevator0.setStop(1, true);
        floors.get(1).setWantUp(true);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());

        elevator0.setFloor(1);
        floors.get(2).setWantDown(true);
        elevator0.setStop(3, true);
        logic.setNextTargets();
        assertEquals(3, elevator0.getTarget());
        elevator0.setStop(1, false);

        elevator0.setFloor(3);
        logic.setNextTargets();
        assertEquals(2, elevator0.getTarget());
        elevator0.setStop(3, false);

        elevator0.setFloor(2);
        elevator0.setStop(1, true);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        floors.get(2).setWantDown(true);
    }

    @Test
    void testAutomaticModeSingleElevatorPressedInsideAndOutsideSequenceWithUnservicedFloor() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(1, 4, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var floors = building.getFloors();
        elevator0.setServiced(3, false);

        elevator0.setStop(1, true);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());

        elevator0.setFloor(1);
        floors.get(2).setWantDown(true);
        elevator0.setStop(3, true);
        logic.setNextTargets();
        assertEquals(2, elevator0.getTarget());
        elevator0.setStop(1, false);

        elevator0.setFloor(2);
        elevator0.setStop(0, true);
        logic.setNextTargets();
        assertEquals(0, elevator0.getTarget());
        floors.get(2).setWantDown(true);

        elevator0.setFloor(0);
        floors.get(3).setWantDown(true);
        floors.get(2).setWantDown(true);
        logic.setNextTargets();
        assertEquals(2, elevator0.getTarget());
    }

    @Test
    void testAutomaticModeMultipleElevatorPressedInsideSequence() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(2, 4, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var elevator1 = building.getElevatorByNumber(1);
        elevator1.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);

        elevator0.setStop(1, true);
        elevator0.setStop(3, true);
        elevator1.setStop(2, true);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        assertEquals(2, elevator1.getTarget());

        elevator0.setFloor(1);
        elevator0.setStop(2, true);
        logic.setNextTargets();
        assertEquals(2, elevator0.getTarget());
        elevator0.setStop(1, false);

        elevator1.setFloor(2);
        elevator1.setStop(1, true);
        elevator1.setStop(0, true);
        logic.setNextTargets();
        assertEquals(1, elevator1.getTarget());
        elevator1.setStop(2, false);

        elevator0.setFloor(2);
        elevator0.setStop(1, true);
        elevator1.setFloor(1);
        logic.setNextTargets();
        assertEquals(3, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
        assertEquals(0, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_DOWN, elevator1.getDirection());
    }

    @Test
    void testAutomaticModeMultipleElevatorPressedOutsideSequence() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(2, 5, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var elevator1 = building.getElevatorByNumber(1);
        elevator1.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var floors = building.getFloors();

        floors.get(2).setWantUp(true);
        floors.get(3).setWantDown(true);
        logic.setNextTargets();
        assertEquals(2, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
        assertEquals(3, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator1.getDirection());

        elevator0.setFloor(2);
        elevator1.setFloor(3);
        floors.get(4).setWantUp(true);
        logic.setNextTargets();
        assertEquals(4, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
        assertEquals(3, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UNCOMMITTED, elevator1.getDirection());
        floors.get(2).setWantUp(false);
        floors.get(3).setWantDown(false);

        elevator0.setFloor(4);
        floors.get(1).setWantDown(true);
        floors.get(1).setWantUp(true);
        floors.get(0).setWantUp(true);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_DOWN, elevator0.getDirection());
        assertEquals(0, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_DOWN, elevator0.getDirection());
        floors.get(4).setWantUp(false);
    }

    @Test
    void testAutomaticModeMultipleElevatorPressedInsideAndOutsideSequence() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(2, 5, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var elevator1 = building.getElevatorByNumber(1);
        elevator1.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var floors = building.getFloors();

        floors.get(2).setWantUp(true);
        floors.get(3).setWantUp(true);
        elevator0.setStop(1, true);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
        assertEquals(2, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator1.getDirection());

        elevator0.setFloor(1);
        elevator1.setFloor(2);
        floors.get(4).setWantUp(true);
        elevator0.setStop(3, true);
        elevator1.setStop(0, true);
        logic.setNextTargets();
        assertEquals(3, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
        assertEquals(4, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator1.getDirection());
        elevator0.setStop(1, false);
        floors.get(2).setWantUp(false);

        elevator0.setFloor(3);
        elevator1.setFloor(4);
        logic.setNextTargets();
        assertEquals(3, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UNCOMMITTED, elevator0.getDirection());
        assertEquals(0, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_DOWN, elevator1.getDirection());
    }

    @Test
    void testAutomaticModeMultipleElevatorPressedInsideAndOutsideSequenceWithUnservicedFloor() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(2, 5, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var elevator1 = building.getElevatorByNumber(1);
        elevator1.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var floors = building.getFloors();
        elevator0.setServiced(3, false);
        elevator1.setServiced(2, false);

        floors.get(2).setWantUp(true);
        floors.get(3).setWantUp(true);
        elevator0.setStop(1, true);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
        assertEquals(3, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator1.getDirection());

        elevator0.setFloor(1);
        elevator1.setFloor(3);
        floors.get(4).setWantUp(true);
        elevator1.setStop(0, true);
        logic.setNextTargets();
        assertEquals(2, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
        assertEquals(4, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator1.getDirection());
        elevator0.setStop(1, false);
        floors.get(3).setWantUp(false);

        elevator0.setFloor(2);
        elevator1.setFloor(4);
        logic.setNextTargets();
        assertEquals(2, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UNCOMMITTED, elevator0.getDirection());
        assertEquals(0, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_DOWN, elevator1.getDirection());
    }

    @Test
    void testSwitchToManualModeAndBack() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(1, 3, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var floors = building.getFloors();

        floors.get(1).setWantDown(true);
        elevator0.setStop(2, true);
        logic.setNextTargets();
        assertEquals(2, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());

        logic.setManual(0, true);
        logic.setElevatorManualTarget(0, 0);
        logic.setNextTargets();
        assertEquals(2, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());

        elevator0.setFloor(2);
        logic.setNextTargets();
        assertEquals(0, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_DOWN, elevator0.getDirection());
        elevator0.setStop(2, false);

        logic.setManual(0, false);
        logic.setNextTargets();
        assertEquals(0, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_DOWN, elevator0.getDirection());

        elevator0.setFloor(0);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
    }

    @Test
    void testAutomaticModeTwoIdleElevatorsAndFloorButtonPressed() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(2, 3, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var elevator1 = building.getElevatorByNumber(1);
        elevator1.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var floors = building.getFloors();

        floors.get(1).setWantDown(true);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
        assertEquals(0, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UNCOMMITTED, elevator1.getDirection());

        floors.get(2).setWantDown(true);
        logic.setNextTargets();
        assertEquals(1, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
        assertEquals(2, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator1.getDirection());
    }

    @Test
    void testAutomaticModeElevatorGoesToTopFloorAndFloorBetweenIsPressed() {
        ModelFactory factory = new ModelFactory(new MockElevatorService(2, 4, 10));
        var building = factory.createBuilding();
        BusinessLogic logic = new BusinessLogic(building);
        var elevator0 = building.getElevatorByNumber(0);
        elevator0.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var elevator1 = building.getElevatorByNumber(1);
        elevator1.setDoorStatus(IElevatorService.ELEVATOR_DOORS_OPEN);
        var floors = building.getFloors();

        elevator0.setStop(3, true);
        logic.setNextTargets();
        assertEquals(3, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
        assertEquals(0, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UNCOMMITTED, elevator1.getDirection());

        elevator0.setFloor(1);
        floors.get(2).setWantUp(true);
        logic.setNextTargets();
        assertEquals(3, elevator0.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator0.getDirection());
        assertEquals(2, elevator1.getTarget());
        assertEquals(IElevatorService.ELEVATOR_DIRECTION_UP, elevator1.getDirection());
    }
}
