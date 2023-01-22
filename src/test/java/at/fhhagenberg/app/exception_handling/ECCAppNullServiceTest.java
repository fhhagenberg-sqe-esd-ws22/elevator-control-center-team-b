package at.fhhagenberg.app.exception_handling;

import at.fhhagenberg.app.ECCApp;
import at.fhhagenberg.service.IElevatorService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
public class ECCAppNullServiceTest extends ECCApp {

    @Override
    protected IElevatorService createService() {
        return null;
    }
}
