package at.fhhagenberg.viewmodels;

import at.fhhagenberg.logic.BusinesLogic;
import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.service.IElevator;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ElevatorViewModel {
    private final Elevator mModel;
    private final BusinesLogic mLogic;
    private final boolean[] mStops;
    // speed of the elevator
    private final SimpleIntegerProperty mSpeed;
    // current acceleration of the elevator
    private final SimpleIntegerProperty mAccel;
    // the next planned elevator stops
    private final SimpleIntegerProperty mTarget;
    // current direction of the elevator
    private final SimpleIntegerProperty mDirection;
    // current weight inside the elevator - persons and luggage
    private final SimpleIntegerProperty mPayload;
    // status of the doors - open/closing/closed
    private final SimpleIntegerProperty mDoorStatus;
    // nearest floor of this elevator
    private final SimpleIntegerProperty mNearestFloor;
    // status of the doors - open/closing/closed as a string
    private final SimpleStringProperty mDoorStatusString;
    // current direction of the elevator as a string
    private final SimpleStringProperty mDirectionString;
    // string that contains all stops - temporary until a refined gui is developed
    private final SimpleStringProperty mStopString;
    // boolean if the elevator is in manual mode
    private final SimpleBooleanProperty mManual;

    public ElevatorViewModel(Elevator elevator, BusinesLogic logic) {
        mModel = elevator;
        mLogic = logic;
        mStops = new boolean[mModel.getNrOfFloors()];
        mSpeed = new SimpleIntegerProperty();
        mAccel = new SimpleIntegerProperty();
        mTarget = new SimpleIntegerProperty();
        mDirection = new SimpleIntegerProperty();
        mPayload = new SimpleIntegerProperty();
        mDoorStatus = new SimpleIntegerProperty();
        mNearestFloor = new SimpleIntegerProperty();
        mDoorStatusString = new SimpleStringProperty();
        mDirectionString = new SimpleStringProperty();
        mStopString = new SimpleStringProperty();
        mManual = new SimpleBooleanProperty(false);

        mManual.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obj, Boolean oldVal, Boolean newVal) {
                mLogic.setManual(getElevatorNr(), newVal);
            }
        });
    }

    public SimpleIntegerProperty getPayloadProp() {
        return mPayload;
    }

    public final SimpleIntegerProperty getSpeedProp() {
        return mSpeed;
    }

    public SimpleIntegerProperty getAccelProp() {
        return mAccel;
    }

    public SimpleIntegerProperty getTargetProp() {
        return mTarget;
    }

    public SimpleIntegerProperty getDirectionProp() {
        return mDirection;
    }

    public SimpleStringProperty getDirectionStringProp() {
        return mDirectionString;
    }

    public SimpleIntegerProperty getDoorStatusProp() {
        return mDoorStatus;
    }

    public SimpleStringProperty getDoorStatusStringProp() {
        return mDoorStatusString;
    }

    public SimpleIntegerProperty getNearestFloorProp() {
        return mNearestFloor;
    }

    public SimpleStringProperty getStopsProp() {
        return mStopString;
    }

    public SimpleBooleanProperty getManualProp() {
        return mManual;
    }

    public final int getSpeed() {
        return mSpeed.get();
    }

    public int getAccel() {
        return mAccel.get();
    }

    public int getTarget() {
        return mTarget.get();
    }

    public int getDirection() {
        return mDirection.get();
    }

    public String getDirectionString() {
        return mDirectionString.get();
    }

    public int getPayload() {
        return mPayload.get();
    }

    public int getDoorStatus() {
        return mDoorStatus.get();
    }

    public String getDoorStatusString() {
        return mDoorStatusString.get();
    }

    public String getStops() {
        return mStopString.get();
    }

    public int getNearestFloor() {
        return mNearestFloor.get();
    }

    public int getElevatorNr() {
        return mModel.getElevatorNr();
    }
    
    public void update() {
        mSpeed.set(mModel.getSpeed());
        mAccel.set(mModel.getAccel());
        mTarget.set(mModel.getTarget());
        mDirection.set(mModel.getDirection());
        mPayload.set(mModel.getPayload());
        mDoorStatus.set(mModel.getDoorStatus());
        mNearestFloor.set(mModel.getNearestFloor());

        String stop = "";

        for (int i = 0; i < mStops.length; i++) {
            mStops[i] = mModel.getStop(i);
            if (mStops[i]) {
                stop += Integer.toString(i) + ", ";
            }
        }

        mStopString.set(stop);

        switch(mDirection.get())
        {
            case IElevator.ELEVATOR_DIRECTION_UP:
                mDirectionString.set("Up");
                break;
            case IElevator.ELEVATOR_DIRECTION_DOWN:
                mDirectionString.set("Down");
                break;
            case IElevator.ELEVATOR_DIRECTION_UNCOMMITTED:
                mDirectionString.set("Uncommited");
                break;
            default:
                mDirectionString.set("Invalid");
        }

        switch(mDoorStatus.get())
        {
            case IElevator.ELEVATOR_DOORS_CLOSED:
                mDoorStatusString.set("Closed");
                break;
            case IElevator.ELEVATOR_DOORS_OPEN:
                mDoorStatusString.set("Open");
                break;
            case IElevator.ELEVATOR_DOORS_CLOSING:
                mDoorStatusString.set("Closing");
                break;
            case IElevator.ELEVATOR_DOORS_OPENING:
                mDoorStatusString.set("Opening");
                break;
            default:
                mDoorStatusString.set("Invalid");
        }
    }
}
