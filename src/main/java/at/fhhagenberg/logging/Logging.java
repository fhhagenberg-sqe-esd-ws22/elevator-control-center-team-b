package at.fhhagenberg.logging;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class responsible for logging
 */
public class Logging {

    private static final Logger mLogger = (Logger) LoggerFactory.getLogger(Logging.class);

    /**
     * Private constructor of Logging, so it cannot be instantiated externally
     */
    private Logging() {
    }

    /**
     * Getter of the projects' logger
     *
     * @return the logger
     */
    public static Logger getLogger() {
        return mLogger;
    }
}
