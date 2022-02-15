package Elevator;

import java.time.LocalDateTime;

/*
 * The Request class is a data structure to represent the requests that are sent from the floor to the elevator through the scheduler.
 * 
 */

public class Request {

    private final LocalDateTime time;
    private final int sourceFloor;
    private final Direction direction;
    private final int destinationFloor;
    private State state;

    
    /*
	 * A constructor for the Request class. The constructor initializes the variables that are necessary for the
	 * describing the request.
	 * 
	 * Input: LocalDateTime, int, Direction, int
	 * Output: none
	 * 
	 */
    public Request(LocalDateTime time, int sourceFloor, Direction direction, int destinationFloor){
        this.time = time;
        this.sourceFloor = sourceFloor;
        this.direction = direction;
        this.destinationFloor = destinationFloor;
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
                "destination: " + destinationFloor + " |";
    }
}