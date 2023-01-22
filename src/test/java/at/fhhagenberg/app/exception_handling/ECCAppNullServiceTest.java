package at.fhhagenberg.app.exception_handling;

import at.fhhagenberg.app.ECCApp;
import at.fhhagenberg.service.IElevatorService;

public class ECCAppNullServiceTest extends ECCApp {

    @Override
    protected IElevatorService createService() {
        return null;
    }
}
