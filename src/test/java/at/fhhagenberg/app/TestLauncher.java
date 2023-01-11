package at.fhhagenberg.app;

import java.util.logging.ConsoleHandler;
import at.fhhagenberg.logging.Logging;
import javafx.application.Application;

public class TestLauncher {
    public static void main(String[] args) {
        var logger = Logging.GET_LOGGER();

        // remove all existing handlers (no need for logfiles in tests)
        for (var handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        // add a console handler, so all logs are printed to the console
        logger.addHandler(new ConsoleHandler());
        Application.launch(TestECCApp.class, args);
    }
}
