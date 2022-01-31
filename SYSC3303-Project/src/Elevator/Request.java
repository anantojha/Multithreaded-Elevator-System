package Elevator;

import java.time.LocalDateTime;

public class Request {

    private final LocalDateTime time;
    private final int sourceFloor;
    private final Direction direction;
    private final int destinationFloor;

    public Request(LocalDateTime time, int sourceFloor, Direction direction, int destinationFloor){
        this.time = time;
        this.sourceFloor = sourceFloor;
        this.direction = direction;
        this.destinationFloor = destinationFloor;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    @Override
    public String toString(){
        return "| Time: " + time + " | " +
                "source: " + sourceFloor + " | " +
                "direction: " + direction + " | " +
                "destination: " + destinationFloor + " |";
    }
}