package at.fhhagenberg.viewmodels;

import at.fhhagenberg.model.Floor;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class FloorViewModel {
    private final Floor mModel;
    private final SimpleBooleanProperty mWantUp;
    private final SimpleBooleanProperty mWantDown;
    private final SimpleStringProperty mWantDownString;
    private final SimpleStringProperty mWantUpString;

    /**
     * Constructor of FloorViewModel
     * @param floor model of the floor which is used to fill the properties
     */
    public FloorViewModel(Floor floor) {
        mModel = floor;
        mWantUp = new SimpleBooleanProperty();
        mWantDown = new SimpleBooleanProperty();
        mWantUpString = new SimpleStringProperty();
        mWantDownString = new SimpleStringProperty();
    }

    public SimpleBooleanProperty getWantUpProp() {
        return mWantUp;
    }

    public SimpleBooleanProperty getWantDownProp() {
        return mWantDown;
    }

    public SimpleStringProperty getWantUpStrProp() {
        return mWantUpString;
    }

    public SimpleStringProperty getWantDownStrProp() {
        return mWantDownString;
    }

    public boolean getWantUp() {
        return mWantUp.get();
    }

    public boolean getWantDown() {
        return mWantDown.get();
    }

    /**
     * Updates the properties with the model values
     */
    public void update() {
        mWantUp.set(mModel.getWantUp());
        mWantDown.set(mModel.getWantDown());

        if (mWantUp.get()) {
            mWantUpString.set("Up");
        }
        else {
            mWantUpString.set("");
        }

        if (mWantDown.get()) {
            mWantDownString.set("Down");
        }
        else {
            mWantDownString.set("");
        }
    }
}
