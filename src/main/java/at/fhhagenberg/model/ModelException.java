/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.model;

/**
 * Exceptions thrown by models
 */
public class ModelException extends RuntimeException {
    /**
     * Constructor of a {@link ModelException}
     *
     * @param message message of the exception
     */
    public ModelException(String message) {
        super(message);
    }
}
