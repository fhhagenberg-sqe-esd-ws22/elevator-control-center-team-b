package at.fhhagenberg.app.exception_handling;

import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import at.fhhagenberg.app.ECCApp;
import at.fhhagenberg.mock_observable.MockExceptionElevatorService;
import at.fhhagenberg.service.IElevatorService;

@ExtendWith(ApplicationExtension.class)
public class ECCAppExceptionTest extends ECCApp {
    public static MockExceptionElevatorService service = new MockExceptionElevatorService();

    @Override
    protected IElevatorService createService() {
        return service;
    }
}
