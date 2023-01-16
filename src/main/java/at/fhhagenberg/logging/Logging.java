package at.fhhagenberg.logging;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class Logging {
    private static final Logger mLogger = (Logger)LoggerFactory.getLogger(Logging.class);

    public static Logger getLogger() {
        return mLogger;
    }
}
