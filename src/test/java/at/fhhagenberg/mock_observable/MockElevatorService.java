package at.fhhagenberg.mock_observable;

import java.util.Arrays;

import at.fhhagenberg.service.IElevatorService;

public class MockElevatorService implements IElevatorService {

    private final int elevatorNum;
    private final int floorNum;
    private final int[] direction;
    private final int[] accel;
    private final boolean[][] elevatorButton;
    private final int[] doorStatus;
    private final int[] elevatorFloor;
    private final int[] position;
    private final int[] speed;
    private final int[] weight;
    private final int[] capacity;
    private final boolean[] floorDown;
    private final boolean[] floorUp;
    private final int floorHeight;
    private final boolean[][] servicedFloors;
    private final int[] target;

    public MockElevatorService(int nrElevator, int nrFloor, int floorHeight) {
        elevatorNum = nrElevator;
        floorNum = nrFloor;
        direction = new int[nrElevator];
        accel = new int[nrElevator];
        elevatorButton = new boolean[nrElevator][nrFloor];
        doorStatus = new int[nrElevator];
        elevatorFloor = new int[nrElevator];
        position = new int[nrElevator];
        speed = new int[nrElevator];
        weight = new int[nrElevator];
        capacity = new int[nrElevator];
        floorDown = new boolean[nrFloor];
        floorUp = new boolean[nrFloor];
        this.floorHeight = floorHeight;
        servicedFloors = new boolean[nrElevator][nrFloor];
        target = new int[nrElevator];
        Arrays.fill(direction, IElevatorService.ELEVATOR_DIRECTION_UNCOMMITTED);
        Arrays.fill(accel, 0);
        Arrays.fill(doorStatus, IElevatorService.ELEVATOR_DOORS_CLOSED);
        Arrays.fill(elevatorFloor, 0);
        Arrays.fill(position, 0);
        Arrays.fill(speed, 0);
        Arrays.fill(weight, 0);
        Arrays.fill(capacity, 0);
        Arrays.fill(floorDown, false);
        Arrays.fill(floorUp, false);
        Arrays.fill(target, 0);
        for (boolean[] i : elevatorButton) {
            Arrays.fill(i, false);
        }
        for (boolean[] i : servicedFloors) {
            Arrays.fill(i, true);
        }
    }

    @Override
    public int getCommittedDirection(int elevatorNumber) {
        return direction[elevatorNumber];
    }

    @Override
    public int getElevatorAccel(int elevatorNumber) {
        return accel[elevatorNumber];
    }

    @Override
    public boolean getElevatorButton(int elevatorNumber, int floor) {
        return elevatorButton[elevatorNumber][floor];
    }

    @Override
    public int getElevatorDoorStatus(int elevatorNumber) {
        return doorStatus[elevatorNumber];
    }

    @Override
    public int getElevatorFloor(int elevatorNumber) {
        return elevatorFloor[elevatorNumber];
    }

    @Override
    public int getElevatorNum() {
        return elevatorNum;
    }

    @Override
    public int getElevatorPosition(int elevatorNumber) {
        return position[elevatorNumber];
    }

    @Override
    public int getElevatorSpeed(int elevatorNumber) {
        return speed[elevatorNumber];
    }

    @Override
    public int getElevatorWeight(int elevatorNumber) {
        return weight[elevatorNumber];
    }

    @Override
    public int getElevatorCapacity(int elevatorNumber) {
        return capacity[elevatorNumber];
    }

    @Override
    public boolean getFloorButtonDown(int floor) {
        return floorDown[floor];
    }

    @Override
    public boolean getFloorButtonUp(int floor) {
        return floorUp[floor];
    }

    @Override
    public int getFloorHeight() {
        return floorHeight;
    }

    @Override
    public int getFloorNum() {
        return floorNum;
    }

    @Override
    public boolean getServicesFloors(int elevatorNumber, int floor) {
        return servicedFloors[elevatorNumber][floor];
    }

    @Override
    public int getTarget(int elevatorNumber) {
        return target[elevatorNumber];
    }

    @Override
    public void setCommittedDirection(int elevatorNumber, int direction) {
        this.direction[elevatorNumber] = direction;
    }

    @Override
    public void setServicesFloors(int elevatorNumber, int floor, boolean service) {
        this.servicedFloors[elevatorNumber][floor] = service;
    }

    @Override
    public void setTarget(int elevatorNumber, int target) {
        this.target[elevatorNumber] = target;
    }

    public void setAccel(int elevatorNumber, int accel) {
        this.accel[elevatorNumber] = accel;
    }

    public void setElevatorButton(int elevatorNum, int floorNum, boolean pressed) {
        this.elevatorButton[elevatorNum][floorNum] = pressed;
    }

    public void setDoorStatus(int elevatorNum, int status) {
        this.doorStatus[elevatorNum] = status;
    }

    public void setElevatorFloor(int elevatorNum, int floorNum) {
        this.elevatorFloor[elevatorNum] = floorNum;
    }

    public void setSpeed(int elevatorNum, int speed) {
        this.speed[elevatorNum] = speed;
    }

    public void setWeight(int elevatorNum, int weight) {
        this.weight[elevatorNum] = weight;
    }

    public void setFloorDown(int floorNum, boolean wantDown) {
        this.floorDown[floorNum] = wantDown;
    }

    public void setFloorUp(int floorNum, boolean wantUp) {
        this.floorUp[floorNum] = wantUp;
    }

    @Override
    public long getClockTick() {
        return 0;
    }
    
}
