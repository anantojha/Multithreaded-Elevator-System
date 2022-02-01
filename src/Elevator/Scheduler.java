package Elevator;

public class Scheduler {
    private Request[] requests = null;
    private boolean requestIsAvailable = false;
    private int requestsCompleted = 0;
    
    // synchronized function for Elevator to retrieve a request
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
        return requests[0];
    }

    // synchronized function for Floor to add new request
    public synchronized void putRequest(Request[] requestsToAdd){
        while (requestIsAvailable) {
            try {
                // make floor wait while a request available
                wait();
            } catch (InterruptedException e) {
                System.out.println("Cannot WAIT on " + this.getClass().getName() + " Thread to place ingredients on Table.");
            }
        }

        // request is added
        requests = requestsToAdd;
        requestIsAvailable = true;

        System.out.println(Thread.currentThread().getName() + " added request: " + requests[0].toString());

        // notify all threads of change
        notifyAll();
    }
    
    // synchronized function for Elevator to set request as complete
    public synchronized void serviceRequest(Request request) throws InterruptedException {
        requestsCompleted++;
        System.out.println("Elevator has completed request #: " + requestsCompleted + "");
        requestIsAvailable = false;             // clear requests
        notifyAll();                            // notify all threads of change
    }



}
