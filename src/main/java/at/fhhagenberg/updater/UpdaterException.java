/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

/**
 * Exceptions thrown by an updater
 */
public class UpdaterException extends RuntimeException {
    /**
     * Constructor of a {@link UpdaterException}
     * @param message message of the exception
     */
    public UpdaterException(String message) {
        super(message);
    }
}
