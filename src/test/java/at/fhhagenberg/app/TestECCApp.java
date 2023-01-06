package at.fhhagenberg.app;

import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import at.fhhagenberg.mock_observable.MockElevatorService;
import at.fhhagenberg.service.IElevatorService;

@ExtendWith(ApplicationExtension.class)
public class TestECCApp extends ECCApp {

    public static MockElevatorService service;

    @Override
    protected IElevatorService createService() {
        service = new MockElevatorService(4, 6, 10);
        return service;
    }
}