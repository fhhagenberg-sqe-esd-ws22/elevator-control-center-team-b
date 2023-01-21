package at.fhhagenberg.logging;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * Class responsible for logging
 */
public class Logging {
    private static final Logger mLogger = (Logger)LoggerFactory.getLogger(Logging.class);

    /**
     * Getter of the projects' logger 
     * @return the logger
     */
    public static Logger getLogger() {
        return mLogger;
    }
}
