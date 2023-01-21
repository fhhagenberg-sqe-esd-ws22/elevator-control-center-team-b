package at.fhhagenberg.viewmodels;

import at.fhhagenberg.logic.BusinessLogic;
import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.view.ElevatorView;
import sqelevator.IElevator;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;

/**
 * ViewModel of an {@link Elevator} that contains properties which are used by an {@link ElevatorView}
 */
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
    // floor where this elevator is at
    private final SimpleIntegerProperty mFloor;
    // status of the doors - open/closing/closed as a string
    private final SimpleStringProperty mDoorStatusString;
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
        mStops = new SimpleObjectProperty<>();
        mServiced = new SimpleObjectProperty<>();
        mSpeed = new SimpleIntegerProperty();
        mAccel = new SimpleIntegerProperty();
        mTarget = new SimpleIntegerProperty();
        mDirection = new SimpleIntegerProperty();
        mPayload = new SimpleIntegerProperty();
        mDoorStatus = new SimpleIntegerProperty();
        mFloor = new SimpleIntegerProperty();
        mDoorStatusString = new SimpleStringProperty();
        mManual = new SimpleBooleanProperty(false);
        mManualFloor = new SimpleIntegerProperty();




        mManualFloor.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> obj, Number oldVal, Number newVal) {
                mLogic.setElevatorManualTarget(getElevatorNr(),newVal.intValue());
            }
        });

        mManual.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obj, Boolean oldVal, Boolean newVal) {
                mLogic.setManual(getElevatorNr(), newVal);
            }
        });
    }

    /**
     * Getter for the payload property
     * @return payload property
     */
    public SimpleIntegerProperty getPayloadProp() {
        return mPayload;
    }

    /**
     * Getter for the speed property
     * @return speed property
     */
    public final SimpleIntegerProperty getSpeedProp() {
        return mSpeed;
    }

    /**
     * Getter for the acceleration property
     * @return acceleration property
     */
    public SimpleIntegerProperty getAccelProp() {
        return mAccel;
    }

    /**
     * Getter for the target property
     * @return target property
     */
    public SimpleIntegerProperty getTargetProp() {
        return mTarget;
    }

    /**
     * Getter for the direction property
     * @return direction property
     */
    public SimpleIntegerProperty getDirectionProp() {
        return mDirection;
    }

    /**
     * Getter for the door status property
     * @return status property
     */
    public SimpleStringProperty getDoorStatusStringProp() {
        return mDoorStatusString;
    }

    /**
     * Getter for the current floor property
     * @return current floor property
     */
    public SimpleIntegerProperty getFloorProp() {
        return mFloor;
    }

    /**
     * Getter for the requested stops property
     * @return requested stops property
     */
    public SimpleObjectProperty<ArrayList<Integer>> getStopsProp() { 
        return mStops; 
    }

    /**
     * Getter for the serviced floors property
     * @return serviced floors property
     */
    public SimpleObjectProperty<ArrayList<Integer>> getServicedProp() {
        return mServiced;
    }

    /**
     * Getter for the manual mode property
     * @return manual mode property
     */
    public SimpleBooleanProperty getManualProp() {
        return mManual;
    }

    /**
     * Getter for the manual floor request property
     * @return floor request property
     */
    public SimpleIntegerProperty getManualFloorProp() {
        return mManualFloor;
    }

    /**
     * Getter for the identifying elevator number
     * @return identifying elevator number
     */
    public int getElevatorNr() {
        return mModel.getElevatorNr();
    }

    /**
     * Getter for the number of floors
     * @return number of floors
     */
    public int getNrFloors() {
        return mModel.getNrOfFloors(); 
    }
    
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
        mFloor.set(mModel.getFloor());

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
                break;
        }
    }
}
