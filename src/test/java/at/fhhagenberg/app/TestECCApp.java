package at.fhhagenberg.app;

import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import at.fhhagenberg.mock_observable.MockElevatorService;
import at.fhhagenberg.service.IElevatorService;

@ExtendWith(ApplicationExtension.class)
public class TestECCApp extends ECCApp {

    private MockElevatorService service;

    @Override
    protected IElevatorService createService() {
        service = new MockElevatorService(4, 6, 10);

        service.setDoorStatus(0, IElevatorService.ELEVATOR_DOORS_CLOSED);
        service.setDoorStatus(1, IElevatorService.ELEVATOR_DOORS_CLOSING);
        service.setDoorStatus(2, IElevatorService.ELEVATOR_DOORS_OPEN);
        service.setDoorStatus(3, IElevatorService.ELEVATOR_DOORS_OPENING);

        service.setElevatorButton(0, 3, true);
        service.setElevatorButton(0, 4, true);
        service.setElevatorButton(1, 0, true);
        service.setElevatorButton(2, 0, true);
        service.setElevatorButton(2, 1, true);
        service.setElevatorButton(2, 2, true);
        service.setElevatorButton(2, 3, true);
        service.setElevatorButton(2, 4, true);
        service.setElevatorButton(2, 5, true);
        service.setElevatorButton(3, 1, true);
        service.setElevatorButton(3, 1, true);
        service.setElevatorButton(3, 4, true);

        service.setServicesFloors(3,4,false);
        service.setServicesFloors(0,1,false);
        service.setServicesFloors(0,2,false);
        service.setServicesFloors(0,3,false);

        service.setSpeed(1, 5);
        service.setSpeed(2, 3);
        service.setPosition(0, 20);
        service.setPosition(1, 5);
        service.setPosition(2, 30);
        service.setAccel(2, -1);
        
        service.setTarget(1, 3);
        service.setTarget(2, 2);

        service.setWeight(0, 10);
        service.setWeight(1, 20);
        service.setWeight(2, 30);
        service.setWeight(3, 40);

        service.setFloorDown(0, true);
        service.setFloorDown(3, true);
        service.setFloorDown(4, true);
        service.setFloorUp(0, true);
        service.setFloorUp(1, true);
        service.setFloorUp(3, true);

        service.setCommittedDirection(1, IElevatorService.ELEVATOR_DIRECTION_UP);
        service.setCommittedDirection(2, IElevatorService.ELEVATOR_DIRECTION_DOWN);

        return service;
    }
}