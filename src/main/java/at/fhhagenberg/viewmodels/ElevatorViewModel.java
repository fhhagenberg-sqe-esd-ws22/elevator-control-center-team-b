package at.fhhagenberg.viewmodels;

import at.fhhagenberg.logic.BusinessLogic;
import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.service.IElevator;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.EventListener;

public class ElevatorViewModel {
    private final Elevator mModel;
    private final BusinessLogic mLogic;
    // buttons pressed in the elevator
    private final SimpleObjectProperty<ArrayList<Integer>> mStops;
    // floors that are serviced by the elevator
    private final SimpleObjectProperty<ArrayList<Integer>> mServiced;
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
    // boolean if the elevator is in manual mode
    private final SimpleBooleanProperty mManual;
    // what floor was selected on manual mode
    private final SimpleIntegerProperty mManualFloor;

    /**
     * Constructor of ElevatorViewModel
     * @param elevator elevator which's properties are copied
     * @param logic logic that controls the elevator
     */
    public ElevatorViewModel(Elevator elevator, BusinessLogic logic) {
        mModel = elevator;
        mLogic = logic;
        mStops = new SimpleObjectProperty<ArrayList<Integer>>();
        mServiced = new SimpleObjectProperty<ArrayList<Integer>>();
        mSpeed = new SimpleIntegerProperty();
        mAccel = new SimpleIntegerProperty();
        mTarget = new SimpleIntegerProperty();
        mDirection = new SimpleIntegerProperty();
        mPayload = new SimpleIntegerProperty();
        mDoorStatus = new SimpleIntegerProperty();
        mNearestFloor = new SimpleIntegerProperty();
        mDoorStatusString = new SimpleStringProperty();
        mDirectionString = new SimpleStringProperty();
        mManual = new SimpleBooleanProperty(false);
        mManualFloor = new SimpleIntegerProperty();


        var listener = new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> obj, Integer oldVal, Integer newVal) {
                mLogic.setElevatorManualTarget(getElevatorNr(),newVal);
            }
        };

        mManualFloor.addListener(listener);

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

    public SimpleStringProperty getDoorStatusStringProp() {
        return mDoorStatusString;
    }

    public SimpleIntegerProperty getNearestFloorProp() {
        return mNearestFloor;
    }

    public SimpleObjectProperty<ArrayList<Integer>> getStopsProp() { return mStops; }

    public SimpleObjectProperty<ArrayList<Integer>> getServicedProp() {
        return mServiced;
    }

    // TODO: test properly
    public SimpleBooleanProperty getManualProp() {
        return mManual;
    }

    // TODO: test properly
    public SimpleIntegerProperty getManualFloorProp() {
        return mManualFloor;
    }

    public int getElevatorNr() {
        return mModel.getElevatorNr();
    }

    public int getNrFloors(){ return mModel.getNrOfFloors(); }
    
    /**
     * Updates all properties of this
     */
    public void update() {
        mSpeed.set(mModel.getSpeed());
        mAccel.set(mModel.getAccel());
        mTarget.set(mModel.getTarget());
        mDirection.set(mModel.getDirection());
        mPayload.set(mModel.getPayload());
        mDoorStatus.set(mModel.getDoorStatus());
        mNearestFloor.set(mModel.getNearestFloor());

        var stops = new ArrayList<Integer>();
        var serviced = new ArrayList<Integer>();
        for(int i = 0; i < mModel.getNrOfFloors(); ++i){
            if(mModel.getStop(i)){
                stops.add(i);
            }
            if(mModel.getServiced(i)){
                serviced.add(i);
            }
        }
        mStops.set(stops);
        mServiced.set(serviced);

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
