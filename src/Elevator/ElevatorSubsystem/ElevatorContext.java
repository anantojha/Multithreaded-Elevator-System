package Elevator.ElevatorSubsystem;

import Elevator.Enums.Direction;
/*
 * The ElevatorContext represents the contextual state of the Elevator relative to the Floors.
 */
public class ElevatorContext {
    private Integer currentFloor;
    private Direction direction;

    /*
     * ElevatorState(Integer) is the constructor of the ElevatorState class. It initializes all necessary variables.
     *
     * Input: Integer startFloor
     * Output: none
     *
     */
    public ElevatorContext(Integer floor, Direction direction){
        this.currentFloor = floor;
        this.direction = direction;
        //this.status = status;
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
    public ElevatorContext setCurrentFloor(Integer currentFloor) {
        this.currentFloor = currentFloor;
        return this;
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
    public ElevatorContext setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }
}
