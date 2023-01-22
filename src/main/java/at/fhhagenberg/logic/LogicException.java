/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.logic;

/**
 * Exceptions thrown by {@link BusinessLogic} and {@link AppController}
 */
public class LogicException extends RuntimeException {
    /**
     * Constructor for {@link LogicException}
     *
     * @param message Exception message.
     */
    public LogicException(String message) {
        super(message);
    }
}
