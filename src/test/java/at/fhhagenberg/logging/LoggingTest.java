package at.fhhagenberg.logging;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class LoggingTest {
    @Test
    void testLoggingNotNull() {
        assertNotNull(Logging.getLogger());
    }

    @Test
    void testLogFolderExists() {
        Logging.getLogger();
        assertTrue(Files.exists(Paths.get("./Logs/")));
    }
}
