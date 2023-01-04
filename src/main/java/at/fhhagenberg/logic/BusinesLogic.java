package at.fhhagenberg.logic;

import java.util.Arrays;

import at.fhhagenberg.model.Building;

/**
 * Class responsible for controlling the elevator
 */
public class BusinesLogic {
    private final Building mModel;
    private final boolean[] mManual;

    /**
     * Constructor of BusinesLogic
     * @param building the building for which the elevators should be controled
     */
    public BusinesLogic(Building building) {
        mModel = building;
        mManual = new boolean[mModel.getElevators().size()];
        Arrays.fill(mManual, false);
    }

    /**
     * Activates or deactivates manual mode of an elevator
     * @param elevatorNr elevator for which manual mode should be set
     * @param active if true, the elevator will be operated in manual mode
     *                  otherwise in automatic mode.
     */
    public void setManual(int elevatorNr, boolean active) {
        mManual[elevatorNr] = active;
    }
}
