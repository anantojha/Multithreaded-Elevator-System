package Elevator.SchedulerSubsystem;

import Elevator.ElevatorSubsystem.ElevatorState;
import Elevator.Enums.SchedulerStatus;
import Elevator.FloorSubsystem.Request;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;
/*
 * SchedulerState represents the State class of the Scheduler
 * 
 */
public class SchedulerState extends Observable {

    private ArrayList<ElevatorState> elevators;
    private Queue<Request> requests;
    private boolean requestIsAvailable;
    private int requestsCompleted;
    private SchedulerStatus state;
    /*
	 * SchedulerState() is the constructor of the SchedulerState class. It initializes all necessary variables.
	 * 
	 * Input: none
	 * Output: none
	 * 
	 */
    public SchedulerState(){
        this.elevators = new ArrayList<>();
        this.requests = new LinkedList<>();
        this.requestIsAvailable = false;
        this.requestsCompleted = 0;
    }
	/*
	 * addElevator(ElevatorState) adds an elevator to the Scheduler
	 * 
	 * Input: none
	 * Output: none
	 * 
	 */
    public void addElevator(ElevatorState elevatorState){
        elevators.add(elevatorState);
    }
	/*
	 * getElevators() gets a list of all elevators.
	 * 
	 * Input: none
	 * Output: ArrayList<ElevatorState> currentFloor
	 * 
	 */
    public ArrayList<ElevatorState> getElevators() {
        return elevators;
    }
	/*
	 * getElevators() gets a queue of all requests.
	 * 
	 * Input: none
	 * Output: Queue<Request> requests
	 * 
	 */
    public Queue<Request> getRequests() {
        return requests;
    }
	/*
	 * isRequestIsAvaiable() returns a boolean value depending on if a request is available or not.
	 * 
	 * Input: none
	 * Output: boolean requestIsAvailable
	 * 
	 */
    public boolean isRequestIsAvailable() {
        return requestIsAvailable;
    }
	/*
	 * setRequestIsAvaiable() sets a boolean value depending on if a request is available or not.
	 * 
	 * Input: boolean requestIsAvailable
	 * Output: none
	 * 
	 */
    public void setRequestIsAvailable(boolean requestIsAvailable) {
        this.requestIsAvailable = requestIsAvailable;
    }
	/*
	 * getRequestsCompleted() gets a count of all requests completed thus far.
	 * 
	 * Input: none
	 * Output: int requestsCompleted
	 * 
	 */
    public int getRequestsCompleted() {
        return requestsCompleted;
    }
	/*
	 * setRequestsCompleted() sets a count of all requests completed thus far.
	 * 
	 * Input: int requestsCompleted
	 * Output: none
	 * 
	 */
    public void setRequestsCompleted(int requestsCompleted) {
        this.requestsCompleted = requestsCompleted;
    }
	/*
	 * setState() sets the current State equal to the parameter
	 * 
	 * Input: SchedulerStatus state
	 * Output: none
	 * 
	 */
    public void setState(SchedulerStatus state) {
    	this.state = state;
    }
	/*
	 * setState() gets the current State equal to the parameter
	 * 
	 * Input: none
	 * Output: SchedulerStatus state
	 * 
	 */
    public String getState() {
    	return this.state.toString();
    }
}
