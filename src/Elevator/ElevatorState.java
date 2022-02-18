package Elevator;

import Elevator.Enums.Direction;
import Elevator.Enums.ElevatorStatus;
import Elevator.Global.SystemConfiguration;
import java.util.Observable;

public class ElevatorState extends Observable {

    private Integer startFloor;
    private Integer currentFloor;
    private Direction direction;
    private ElevatorStatus status;
    private Integer maxFloor;

    public ElevatorState(Integer startFloor){
        this.startFloor = startFloor;
        this.currentFloor = startFloor;
        this.direction = Direction.UP;
        this.status = ElevatorStatus.IDLE;
        this.maxFloor = SystemConfiguration.MAX_FLOOR;
    }

    public Integer getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(Integer currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public ElevatorStatus getStatus() {
        return status;
    }

    public void setStatus(ElevatorStatus status) {
        this.status = status;
    }

    public Integer getMaxFloor() {
        return maxFloor;
    }
}
