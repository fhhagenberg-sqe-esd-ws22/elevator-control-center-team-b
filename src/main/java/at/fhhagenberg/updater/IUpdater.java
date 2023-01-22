/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.updater;

/**
 * Interface for updaters
 */
public interface IUpdater {
    /**
     * Performs all necessary API calls on a service object in order to update a referenced model object.
     */
    void update();
}
