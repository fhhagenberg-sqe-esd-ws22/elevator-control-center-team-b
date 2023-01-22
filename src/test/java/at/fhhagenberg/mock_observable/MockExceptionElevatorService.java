package at.fhhagenberg.mock_observable;

import at.fhhagenberg.service.IElevatorService;

// class to force the app not to start - return null per default
public class MockExceptionElevatorService implements IElevatorService {

    @Override
    public boolean connect() {
        return false;
    }

    @Override
    public int getCommittedDirection(int elevatorNumber) {
        return 0;
    }

    @Override
    public int getElevatorAccel(int elevatorNumber) {
        return 0;
    }

    @Override
    public boolean getElevatorButton(int elevatorNumber, int floor) {
        return false;
    }

    @Override
    public int getElevatorDoorStatus(int elevatorNumber) {
        return 0;
    }

    @Override
    public int getElevatorFloor(int elevatorNumber) {
        return 0;
    }

    @Override
    public int getElevatorNum() {
        return 0;
    }

    @Override
    public int getElevatorPosition(int elevatorNumber) {
        return 0;
    }

    @Override
    public int getElevatorSpeed(int elevatorNumber) {
        return 0;
    }

    @Override
    public int getElevatorWeight(int elevatorNumber) {
        return 0;
    }

    @Override
    public int getElevatorCapacity(int elevatorNumber) {
        return 0;
    }

    @Override
    public boolean getFloorButtonDown(int floor) {
        return false;
    }

    @Override
    public boolean getFloorButtonUp(int floor) {
        return false;
    }

    @Override
    public int getFloorHeight() {
        return 0;
    }

    @Override
    public int getFloorNum() {
        return 0;
    }

    @Override
    public boolean getServicesFloors(int elevatorNumber, int floor) {
        return false;
    }

    @Override
    public int getTarget(int elevatorNumber) {
        return 0;
    }

    @Override
    public void setCommittedDirection(int elevatorNumber, int direction) {
    }

    @Override
    public void setServicesFloors(int elevatorNumber, int floor, boolean service) {
    }

    @Override
    public void setTarget(int elevatorNumber, int target) {
    }

    @Override
    public long getClockTick() {
        return 0;
    }

}
