/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.service;

import java.rmi.RemoteException;
import java.util.logging.Level;

import at.fhhagenberg.logging.Logging;
import sqelevator.IElevator;

public class RMIElevatorService implements IElevatorService{
    private final IElevator mApi;

    private static final String CLASS_NAME = "RMIElevatorService";
    private static final String ERROR_MESSAGE_LITERAL = "! \nError message: ";

    public RMIElevatorService(IElevator api) {
        if (api == null) {
            String errorMsg = "Could not create RMIElevatorService, the given IElevator object is null!";
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                CLASS_NAME, errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
        mApi = api;
    }

    @Override
    public int getCommittedDirection(int elevatorNumber) throws ElevatorServiceException {
        try {
            return mApi.getCommittedDirection(elevatorNumber);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the committed direction for elevator " + elevatorNumber + ERROR_MESSAGE_LITERAL +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getCommittedDirection", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public int getElevatorAccel(int elevatorNumber) throws ElevatorServiceException  {
        try {
            return mApi.getElevatorAccel(elevatorNumber);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the acceleration for elevator " + elevatorNumber + ERROR_MESSAGE_LITERAL +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getElevatorAccel", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public boolean getElevatorButton(int elevatorNumber, int floor) throws ElevatorServiceException  {
        try {
            return mApi.getElevatorButton(elevatorNumber, floor);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the state of the button for elevator " + elevatorNumber + "on floor" + floor + ERROR_MESSAGE_LITERAL +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getElevatorButton", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public int getElevatorDoorStatus(int elevatorNumber) throws ElevatorServiceException  {
        try {
            return mApi.getElevatorDoorStatus(elevatorNumber);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the door status for elevator " + elevatorNumber + ERROR_MESSAGE_LITERAL +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getElevatorDoorStatus", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public int getElevatorFloor(int elevatorNumber) throws ElevatorServiceException  {
        try {
            return mApi.getElevatorFloor(elevatorNumber);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the door status for elevator " + elevatorNumber + ERROR_MESSAGE_LITERAL +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getElevatorFloor", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public int getElevatorNum() throws ElevatorServiceException  {
        try {
            return mApi.getElevatorNum();
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the number of elevators!" + ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getElevatorNum", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public int getElevatorPosition(int elevatorNumber) throws ElevatorServiceException  {
        try {
            return mApi.getElevatorPosition(elevatorNumber);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the position for elevator " + elevatorNumber + ERROR_MESSAGE_LITERAL +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME, 
                "getElevatorPosition", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public int getElevatorSpeed(int elevatorNumber) throws ElevatorServiceException  {
        try {
            return mApi.getElevatorSpeed(elevatorNumber);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the speed for elevator " + elevatorNumber + ERROR_MESSAGE_LITERAL +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getElevatorSpeed", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public int getElevatorWeight(int elevatorNumber) throws ElevatorServiceException  {
        try {
            return mApi.getElevatorWeight(elevatorNumber);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the weight for elevator " + elevatorNumber + ERROR_MESSAGE_LITERAL +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getElevatorWeight", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public int getElevatorCapacity(int elevatorNumber) throws ElevatorServiceException  {
        try {
            return mApi.getElevatorCapacity(elevatorNumber);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the capacity for elevator " + elevatorNumber + ERROR_MESSAGE_LITERAL +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getElevatorCapacity", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public boolean getFloorButtonDown(int floor) throws ElevatorServiceException  {
        try {
            return mApi.getFloorButtonDown(floor);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the state of the down button for floor " + floor + ERROR_MESSAGE_LITERAL +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getFloorButtonDown", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public boolean getFloorButtonUp(int floor) throws ElevatorServiceException  {
        try {
            return mApi.getFloorButtonUp(floor);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the state of the up button for floor " + floor + ERROR_MESSAGE_LITERAL +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getFloorButtonUp", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public int getFloorHeight() throws ElevatorServiceException  {
        try {
            return mApi.getFloorHeight();
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the height of the floors!\nError message: " +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getFloorHeight", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public int getFloorNum() throws ElevatorServiceException  {
        try {
            return mApi.getFloorNum();
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the number of floors!\nError message: " +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME, 
                "getFloorNum", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public boolean getServicesFloors(int elevatorNumber, int floor) throws ElevatorServiceException  {
        try {
            return mApi.getServicesFloors(elevatorNumber, floor);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the state of service for " + floor + " by elevator " + elevatorNumber + ERROR_MESSAGE_LITERAL +  ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getServicesFloors", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public int getTarget(int elevatorNumber) throws ElevatorServiceException  {
        try {
            return mApi.getTarget(elevatorNumber);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to retrieve the state of the target floor for elevator " + elevatorNumber + ERROR_MESSAGE_LITERAL + ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getTarget", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public void setCommittedDirection(int elevatorNumber, int direction) throws ElevatorServiceException  {
        try {
            mApi.setCommittedDirection(elevatorNumber, direction);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to set the committed direction for elevator " + elevatorNumber + ERROR_MESSAGE_LITERAL + ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "setCommittedDirection", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public void setServicesFloors(int elevatorNumber, int floor, boolean service) throws ElevatorServiceException  {
        try {
            mApi.setServicesFloors(elevatorNumber, floor, service);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to set the service state for floor" + floor + " on elevator " + elevatorNumber + "to " + service + ERROR_MESSAGE_LITERAL + ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "setServicesFloors", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public void setTarget(int elevatorNumber, int target) throws ElevatorServiceException  {
        try {
            mApi.setTarget(elevatorNumber, target);
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to set the target floor to" + target + " on elevator " + elevatorNumber + ERROR_MESSAGE_LITERAL + ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "setTarget", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }

    @Override
    public long getClockTick() throws ElevatorServiceException  {
        try {
            return mApi.getClockTick();
        }
        catch(RemoteException ex) {
            String errorMsg = "An error occurred while trying to receive the clock tick!\nError message: " + ex.getMessage();
            Logging.getLogger().logp(Level.SEVERE, CLASS_NAME,
                "getClockTick", errorMsg);
            throw new ElevatorServiceException(errorMsg);
        }
    }
}
