package at.fhhagenberg.app.exception_handling;

import at.fhhagenberg.app.ECCApp;
import at.fhhagenberg.mock_observable.MockExceptionElevatorService;
import at.fhhagenberg.service.IElevatorService;

public class ECCAppExceptionTest extends ECCApp {
    public static MockExceptionElevatorService service = new MockExceptionElevatorService();

    @Override
    protected IElevatorService createService() {
        return service;
    }
}
