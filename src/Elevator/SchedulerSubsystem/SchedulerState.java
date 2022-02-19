package Elevator.SchedulerSubsystem;

import Elevator.ElevatorSubsystem.ElevatorState;
import Elevator.Enums.SchedulerStatus;
import Elevator.FloorSubsystem.Request;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

public class SchedulerState extends Observable {

    private ArrayList<ElevatorState> elevators;
    private Queue<Request> requests;
    private boolean requestIsAvailable;
    private int requestsCompleted;
    private SchedulerStatus state;

    public SchedulerState(){
        this.elevators = new ArrayList<>();
        this.requests = new LinkedList<>();
        this.requestIsAvailable = false;
        this.requestsCompleted = 0;
    }

    public void addElevator(ElevatorState elevatorState){
        elevators.add(elevatorState);
    }

    public ArrayList<ElevatorState> getElevators() {
        return elevators;
    }

    public Queue<Request> getRequests() {
        return requests;
    }

    public boolean isRequestIsAvailable() {
        return requestIsAvailable;
    }

    public void setRequestIsAvailable(boolean requestIsAvailable) {
        this.requestIsAvailable = requestIsAvailable;
    }

    public int getRequestsCompleted() {
        return requestsCompleted;
    }

    public void setRequestsCompleted(int requestsCompleted) {
        this.requestsCompleted = requestsCompleted;
    }
    public void setState(SchedulerStatus state) {
    	this.state = state;
    }
    public String getState() {
    	return this.state.toString();
    }
}
