/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.service;

/**
 * Exceptions thrown by an {@link IElevatorService}
 */
public class ElevatorServiceException extends RuntimeException {
    /**
     * Constructor of a {@link ElevatorServiceException}
     *
     * @param msg msg of the exception
     */
    ElevatorServiceException(String msg) {
        super(msg);
    }
}
