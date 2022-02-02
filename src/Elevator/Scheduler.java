package Elevator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/*
 * The Scheduler class is a shared data structure between the Elevator and the Floor. It serves to facilitate communicate between both threads.
 * 
 */
public class Scheduler {
    private Queue<Request> requests = new LinkedList<>();
    private boolean requestIsAvailable = false;
    private int requestsCompleted = 0;

    public int getRequestsCompleted() {
        return requestsCompleted;
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
        while(!requestIsAvailable) {
            try {
                // make elevator wait while table is empty
                wait();
            } catch (InterruptedException e) {
                System.out.println("Cannot WAIT on "+ this.getClass().getName() + " Thread to get available requests");
            }
        }

        // notify all threads of change to active requests list
        notifyAll();
        return requests.peek();
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
        while (requestIsAvailable) {
            try {
                // make floor wait while a request available
                wait();
            } catch (InterruptedException e) {
                System.out.println("Cannot WAIT on " + this.getClass().getName() + " Thread to put new requests.");
            }
        }

        // request is added
        requests.add(requestToAdd);
        requestIsAvailable = true;

        System.out.println("Scheduler: " + Thread.currentThread().getName() + " added request: " + requests.peek().toString());

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
    // synchronized function for Elevator to set request as complete
    public synchronized void serviceRequest(Request request, int id) throws InterruptedException {
        requestsCompleted++;
        System.out.println("Scheduler: Elevator " + id + " has completed request #: " + requestsCompleted + "");
        System.out.println();
        requests.poll();

        if(requests.size() == 0)
            requestIsAvailable = false;             // clear requests
        notifyAll();                            // notify all threads of change
    }
}
