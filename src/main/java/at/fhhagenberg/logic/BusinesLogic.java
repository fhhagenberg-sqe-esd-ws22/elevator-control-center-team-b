package at.fhhagenberg.logic;

import java.lang.reflect.Array;
import java.util.Arrays;

import at.fhhagenberg.model.Building;

public class BusinesLogic {
    private final Building mModel;
    private final boolean[] mManual;

    public BusinesLogic(Building building) {
        mModel = building;
        mManual = new boolean[mModel.getElevators().size()];
        Arrays.fill(mManual, false);
    }

    public void setManual(int elevatorNr, boolean active) {
        mManual[elevatorNr] = active;
        System.out.println(String.format("Elevator %d is now %b", elevatorNr, active));
    }

    public boolean[] getManual() {
        return mManual;
    }
}
