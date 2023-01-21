package at.fhhagenberg.viewmodels;

import at.fhhagenberg.model.Floor;
import at.fhhagenberg.view.FloorView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * ViewModel of a {@link Floor} that contains properties which are used by a {@link FloorView}
 */
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

    /**
     * Getter for the want up property
     * @return want up property
     */
    public SimpleBooleanProperty getWantUpProp() {
        return mWantUp;
    }

    /**
     * Getter for the want down property
     * @return want down property
     */
    public SimpleBooleanProperty getWantDownProp() {
        return mWantDown;
    }

    /**
     * Getter for the string property that shows want up
     * @return string property that shows want up
     */
    public SimpleStringProperty getWantUpStrProp() {
        return mWantUpString;
    }

    /**
     * Getter for the string property that shows want down
     * @return string property that shows want down
     */
    public SimpleStringProperty getWantDownStrProp() {
        return mWantDownString;
    }

    /**
     * Getter for want up
     * @return want up
     */
    public boolean getWantUp() {
        return mWantUp.get();
    }

    /**
     * Getter for want down
     * @return want down
     */
    public boolean getWantDown() {
        return mWantDown.get();
    }

    /**
     * Getter for the floor number
     * @return floor number
     */
    public int getFloorNumber() {
        return mModel.getFloorNumber();
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
