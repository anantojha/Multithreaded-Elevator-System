package Elevator.ElevatorSubsystem;

import Elevator.Enums.Direction;
import Elevator.Enums.ElevatorStatus;
import Elevator.Global.SystemConfiguration;

import java.util.Observable;

/*
 * ElevatorState represents the State class of the Elevator
 * 
 */
public class ElevatorState {

    private Integer currentFloor;
    private Direction direction;
    private ElevatorStatus status;
    private Integer maxFloor;

	/*
	 * ElevatorState(Integer) is the constructor of the ElevatorState class. It initializes all necessary variables.
	 * 
	 * Input: Integer startFloor
	 * Output: none
	 * 
	 */
    public ElevatorState(Integer startFloor){
        this.currentFloor = startFloor;
        this.direction = Direction.UP;
        this.status = ElevatorStatus.IDLE;
        this.maxFloor = SystemConfiguration.MAX_FLOOR;
    }

	/*
	 * getCurrentFloor() gets the current floor.
	 * 
	 * Input: none
	 * Output: Integer currentFloor
	 * 
	 */
    public Integer getCurrentFloor() {
        return currentFloor;
    }

	/*
	 * setCurrentFloor(Integer) sets the current floor equal to the parameter
	 * 
	 * Input: Integer currentFloor
	 * Output: none
	 * 
	 */
    public void setCurrentFloor(Integer currentFloor) {
        this.currentFloor = currentFloor;
    }

	/*
	 * getDirection() gets the current direction.
	 * 
	 * Input: none
	 * Output: Direction direction
	 * 
	 */
    public Direction getDirection() {
        return direction;
    }

	/*
	 * setDirection() sets the current direction equal to the parameter.
	 * 
	 * Input: Direction direction
	 * Output: none
	 * 
	 */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

	/*
	 * getStatus() gets the maximum floor.
	 * 
	 * Input: none
	 * Output: ElevatorStatus status
	 * 
	 */
    public ElevatorStatus getStatus() {
        return status;
    }

    /*
	 * setStatus(ElevatorMachineStatus) sets the current state equal to paramater.
	 * 
	 * Input: ElevatorMachineStatus state
	 * Output: none
	 * 
	 */
    public void setStatus(ElevatorStatus status) {
        this.status = status;
    }

	/*
	 * getMaxFloor() gets the maximum floor.
	 * 
	 * Input: none
	 * Output: Integer
	 * 
	 */
    public Integer getMaxFloor() {
        return maxFloor;
    }
}
