package at.fhhagenberg.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Logging {
    private static final String LOGGER_NAME = "ECCLogger";
    private static Logger mLogger = null;
    
    private static void Init() {
        mLogger = Logger.getLogger(LOGGER_NAME);
        try {
            String loggingDirectory = "./Logs/";
            String fileName = String.format("%s.log", new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date()));
            if (!Files.exists(Paths.get(loggingDirectory))) {
                Files.createDirectory(Paths.get(loggingDirectory));
            }

            FileHandler handler;
            handler = new FileHandler(loggingDirectory + fileName);
            mLogger.addHandler(handler);
        }
        catch (SecurityException e) {}
        catch (IOException e) {}
    }

    public static Logger getLogger() {
        if (mLogger == null) {
            Init();
        }
        return mLogger;
    }
}
