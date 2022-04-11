package Elevator.FloorSubsystem;

import Elevator.Enums.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * The Request class is a data structure to represent the requests that are sent from the floor to the elevator through the scheduler.
 * 
 */

public class Request implements Serializable {

    private final LocalDateTime time;
    private final int sourceFloor;
    private final Direction direction;
    private final int destinationFloor;
	private Boolean fault;
	private char faulttype;
    
    /*
	 * A constructor for the Request class. The constructor initializes the variables that are necessary for the
	 * describing the request.
	 * 
	 * Input: LocalDateTime, int, Direction, int
	 * Output: none
	 * 
	 */
    public Request(LocalDateTime time, int sourceFloor, Direction direction, int destinationFloor, String fault){
        this.time = time;
        this.sourceFloor = sourceFloor;
        this.direction = direction;
        this.destinationFloor = destinationFloor;
		this.fault = fault.contains("f") || fault.contains("t");
		this.faulttype = fault.charAt(0);
    }
    
    /*
   	 * getTime() is a getter method for the variable time.
   	 * 
   	 * Input: none
   	 * Output: LocalDateTime time
   	 * 
   	 */
    public LocalDateTime getTime() {
        return time;
    }

    /*
   	 * getSourceFloor() is a getter method for the variable sourceFloor
   	 * 
   	 * Input: none
   	 * Output: int sourceFloor
   	 * 
   	 */
    public int getSourceFloor() {
        return sourceFloor;
    }

	public byte getFaultByte() { return (byte) (faulttype == 'f' ? 1 : faulttype == 't' ? 2 : 0); }

	public boolean getFault() { return fault; }
	public char getFaultType() { return faulttype; }

    /*
   	 * getDirection() is a getter method for the variable direction.
   	 * 
   	 * Input: none
   	 * Output: Direction direction
   	 * 
   	 */
    public Direction getDirection() {
        return direction;
    }

    /*
   	 * getDestinationFloor() is a getter method for the variable destinationfloor.
   	 * 
   	 * Input: none
   	 * Output: int destinationFloor
   	 * 
   	 */
    public int getDestinationFloor() {
        return destinationFloor;
    }

    /*
   	 * toString() is a method to print out all the vairables.
   	 * 
   	 * Input: none
   	 * Output: String
   	 * 
   	 */
    @Override
    public String toString(){
        return "| Time: " + time + " | " +
                "source: " + sourceFloor + " | " +
                "direction: " + direction + " | " +
                "destination: " + destinationFloor + " | " +
				"fault: " + fault.toString() + " |";
    }

	public String toRequestDisplayString(){
		return "source: " + sourceFloor + " | " +
				"direction: " + direction + " | " +
				"destination: " + destinationFloor + " | " +
				"fault: " + fault.toString() + " |";
	}
}