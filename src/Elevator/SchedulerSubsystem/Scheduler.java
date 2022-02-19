package Elevator.SchedulerSubsystem;

import Elevator.ElevatorSubsystem.ElevatorState;
import Elevator.Enums.SchedulerStatus;
import Elevator.FloorSubsystem.Request;

import java.util.LinkedList;
import java.util.Queue;

/*
 * The Scheduler class is a shared data structure between the Elevator and the Floor. It serves to facilitate communicate between both threads.
 * 
 */
public class Scheduler {

    private SchedulerState state = new SchedulerState();

    public int getRequestsCompleted() {
    	state.setState(SchedulerStatus.PRINTREQUEST);
        return state.getRequestsCompleted();
    }

    public void addElevatorState(ElevatorState elevatorState){
    	state.setState(SchedulerStatus.INITIALIZE);
        state.addElevator(elevatorState);
    }

    /*
	 * getRequest() is a method that retrieves a request for the Elevator if some request is available. If none are available,
	 * the Elevator must wait until a request is present.
	 * 
	 * Input: none
	 * Output: Request requests[0] 
	 * 
	 */
    public synchronized Request getRequest() {
    	state.setState(SchedulerStatus.PRINTREQUEST);
        while(!state.isRequestIsAvailable()) {
            try {
                // make elevator wait while table is empty
                wait();
            } catch (InterruptedException e) {
                System.out.println("Cannot WAIT on "+ this.getClass().getName() + " Thread to get available requests");
            }
        }

        // notify all threads of change to active requests list
        notifyAll();
        return state.getRequests().peek();
    }
    
    /*
	 * putRequest() is a method that adds a request for the Elevator if no request is available. If some are available,
	 * the Floor must wait until no request is present.
	 * 
	 * Input: Request[] requestsToAdd
	 * Output: none
	 * 
	 */
    public synchronized void putRequest(Request requestToAdd){
    	state.setState(SchedulerStatus.ADDINGREQUEST);
        while (state.isRequestIsAvailable()) {
            try {
                // make floor wait while a request available
                wait();
            } catch (InterruptedException e) {
                System.out.println("Cannot WAIT on " + this.getClass().getName() + " Thread to put new requests.");
            }
        }

        // request is added
        state.getRequests().add(requestToAdd);
        state.setRequestIsAvailable(true);

        System.out.println("Scheduler: " + Thread.currentThread().getName() + " added request: " + state.getRequests().peek().toString());

        // notify all threads of change
        notifyAll();
    }
    
    /*
	 * serviceRequest() is a method that sets a request for the Elevator as complete. It is the Elevator's consume function.
	 * 
	 * Input: Request request, int id
	 * Output: none
	 * 
	 */
    public synchronized void serviceRequest(Request request, int id) throws InterruptedException {
    	state.setState(SchedulerStatus.COMPLETEREQUEST);
        state.setRequestsCompleted(state.getRequestsCompleted() + 1);
        System.out.println("Scheduler: Elevator " + id + " has completed request #: " + state.getRequestsCompleted() + "");
        System.out.println();
        state.getRequests().poll();

        if(state.getRequests().size() == 0)
            state.setRequestIsAvailable(false);             // clear requests
        notifyAll();                            // notify all threads of change
    }
}
