package at.fhhagenberg.app;

import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

import at.fhhagenberg.logging.Logging;
import javafx.application.Application;

public class TestLauncher {
    public static void main(String[] args) {
        var logger = Logging.getLogger();

        // remove all existing handlers (no need for logfiles in tests)
        for (var handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        // add a console handler, so all logs are printed to the console
        var handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        logger.addHandler(handler);
        Application.launch(TestECCApp.class, args);
    }
}
