package at.fhhagenberg.app;

import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import at.fhhagenberg.mock_observable.MockElevatorService;
import at.fhhagenberg.service.IElevatorService;

@ExtendWith(ApplicationExtension.class)
public class TestECCApp extends ECCApp {

    private final IElevatorService service;

    public
    TestECCApp(IElevatorService service) {
        this.service = service;
    }

    public
    TestECCApp() {
        // default service
        this(new MockElevatorService(4, 6, 10));
    }

    @Override
    protected IElevatorService createService() {
        return service;
    }
}