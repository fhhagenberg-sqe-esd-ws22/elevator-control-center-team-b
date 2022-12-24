/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.service;

import java.rmi.RemoteException;

public class RMIElevatorService implements IElevatorService{
    private final IElevator mApi;

    private static final String mErrorMessageLiteral = "! \nError message: ";

    public RMIElevatorService(IElevator api) {
        if (api == null) {
            throw new ElevatorServiceException("COuld not create RMIElevatorService, the given IElevator object is null!");
        }
        mApi = api;
    }

    @Override
    public int getCommittedDirection(int elevatorNumber) {
        try {
            return mApi.getCommittedDirection(elevatorNumber);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the committed direction for elevator " + elevatorNumber + mErrorMessageLiteral +  ex.getMessage());
        }
    }

    @Override
    public int getElevatorAccel(int elevatorNumber) {
        try {
            return mApi.getElevatorAccel(elevatorNumber);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the acceleration for elevator " + elevatorNumber + mErrorMessageLiteral +  ex.getMessage());
        }
    }

    @Override
    public boolean getElevatorButton(int elevatorNumber, int floor) {
        try {
            return mApi.getElevatorButton(elevatorNumber, floor);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the state of the button for elevator " + elevatorNumber + "on floor" + floor + mErrorMessageLiteral +  ex.getMessage());
        }
    }

    @Override
    public int getElevatorDoorStatus(int elevatorNumber) {
        try {
            return mApi.getElevatorDoorStatus(elevatorNumber);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the door status for elevator " + elevatorNumber + mErrorMessageLiteral +  ex.getMessage());
        }
    }

    @Override
    public int getElevatorFloor(int elevatorNumber) {
        try {
            return mApi.getElevatorFloor(elevatorNumber);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the door status for elevator " + elevatorNumber + mErrorMessageLiteral +  ex.getMessage());
        }
    }

    @Override
    public int getElevatorNum() {
        try {
            return mApi.getElevatorNum();
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the number of elevators!" + ex.getMessage());
        }
    }

    @Override
    public int getElevatorPosition(int elevatorNumber) {
        try {
            return mApi.getElevatorPosition(elevatorNumber);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the position for elevator " + elevatorNumber + mErrorMessageLiteral +  ex.getMessage());
        }
    }

    @Override
    public int getElevatorSpeed(int elevatorNumber) {
        try {
            return mApi.getElevatorSpeed(elevatorNumber);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the speed for elevator " + elevatorNumber + mErrorMessageLiteral +  ex.getMessage());
        }
    }

    @Override
    public int getElevatorWeight(int elevatorNumber) {
        try {
            return mApi.getElevatorWeight(elevatorNumber);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the weight for elevator " + elevatorNumber + mErrorMessageLiteral +  ex.getMessage());
        }
    }

    @Override
    public int getElevatorCapacity(int elevatorNumber) {
        try {
            return mApi.getElevatorCapacity(elevatorNumber);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the capacity for elevator " + elevatorNumber + mErrorMessageLiteral +  ex.getMessage());
        }
    }

    @Override
    public boolean getFloorButtonDown(int floor) {
        try {
            return mApi.getFloorButtonDown(floor);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the state of the down button for floor " + floor + mErrorMessageLiteral +  ex.getMessage());
        }
    }

    @Override
    public boolean getFloorButtonUp(int floor) {
        try {
            return mApi.getFloorButtonUp(floor);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the state of the up button for floor " + floor + mErrorMessageLiteral +  ex.getMessage());
        }
    }

    @Override
    public int getFloorHeight() {
        try {
            return mApi.getFloorHeight();
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the height of the floors!\nError message: " +  ex.getMessage());
        }
    }

    @Override
    public int getFloorNum() {
        try {
            return mApi.getFloorNum();
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the number of floors!\nError message: " +  ex.getMessage());
        }
    }

    @Override
    public boolean getServicesFloors(int elevatorNumber, int floor) {
        try {
            return mApi.getServicesFloors(elevatorNumber, floor);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the state of service for " + floor + " by elevator " + elevatorNumber + mErrorMessageLiteral +  ex.getMessage());
        }
    }

    @Override
    public int getTarget(int elevatorNumber) {
        try {
            return mApi.getTarget(elevatorNumber);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to retrieve the state of the target floor for elevator " + elevatorNumber + mErrorMessageLiteral + ex.getMessage());
        }
    }

    @Override
    public void setCommittedDirection(int elevatorNumber, int direction) {
        try {
            mApi.setCommittedDirection(elevatorNumber, direction);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to set the committed direction for elevator " + elevatorNumber + mErrorMessageLiteral + ex.getMessage());
        }
    }

    @Override
    public void setServicesFloors(int elevatorNumber, int floor, boolean service) {
        try {
            mApi.setServicesFloors(elevatorNumber, floor, service);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to set the service state for floor" + floor + " on elevator " + elevatorNumber + "to " + service + mErrorMessageLiteral + ex.getMessage());
        }
    }

    @Override
    public void setTarget(int elevatorNumber, int target) {
        try {
            mApi.setTarget(elevatorNumber, target);
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to set the target floor to" + target + " on elevator " + elevatorNumber + mErrorMessageLiteral + ex.getMessage());
        }
    }

    @Override
    public long getClockTick() {
        try {
            return mApi.getClockTick();
        }
        catch(RemoteException ex) {
            throw new ElevatorServiceException("An error occurred while trying to receive the clock tick!\nError message: " + ex.getMessage());
        }
    }
}
