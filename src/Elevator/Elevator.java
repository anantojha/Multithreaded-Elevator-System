package Elevator;

/*
 * The Elevator class represents the consumer side of the algorithm. It is responsible for accessing the requests sent to the scheduler
 * and fulfilling them given that the correct conditions are met.
 * 
 */
public class Elevator implements Runnable{

    private Scheduler scheduler;
    private int Id;
    private int currentFloor;
    private boolean testEnabled = false;
    private State state;

    public void setTestEnabled(boolean testEnabled) {
        this.testEnabled = testEnabled;
    }

	/*
	 * A constructor for the Elevator class. The constructor initializes the shared data structure and sets the id
	 * of the Elevator. Each elevator starts from floor 1.
	 * 
	 * Input: Schedule, int
	 * Output: none
	 * 
	 */
    public Elevator(Scheduler scheduler, int id){
        this.scheduler = scheduler;
        this.Id = id;
        this.currentFloor = 1;
    }

    public int getId() {
        return Id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    /*
	 * The run() method is the primary sequence that is run when a thread is active. In this case, the Elevator class will
	 * attempt to receive requests from the scheduler and fulfill them if the request floor is the same as the thread floor.
	 * 
	 * Input: none
	 * Output: none
	 * 
	 */

    @Override
    public void run() {
        while(true) {
            // synchronize with scheduler
            synchronized (scheduler) {
                // get request from scheduler
                Request serviceRequest = scheduler.getRequest();
                
                // elevator is servicing request
                System.out.println(Thread.currentThread().getName() +" is servicing "+ serviceRequest);

                // Move to source floor if not already on source floor
                if (serviceRequest.getSourceFloor() != currentFloor) {
                    try {
                        // go to source floor
                        if(currentFloor > serviceRequest.getSourceFloor()){
                            for(int i = Math.abs(serviceRequest.getSourceFloor() - currentFloor); i >= 1 ; i--){
                                Thread.sleep(600);
                                System.out.println(Thread.currentThread().getName() +" is at floor: "+ (i+1));
                            }
                        }
                        
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
              
                try {
                    // go from source floor to destination floor
                    for(int i = 0; i <= (serviceRequest.getDestinationFloor() - serviceRequest.getSourceFloor()); i++){
                        Thread.sleep(600);
                        if ((i+1) == 1)
                            System.out.println(Thread.currentThread().getName() +" is at floor: "+ (i+1) + " - pickup");
                        else if ((i+1) == serviceRequest.getDestinationFloor())
                            System.out.println(Thread.currentThread().getName() +" is at floor: "+ (i+1) + " - dropoff");
                        else
                            System.out.println(Thread.currentThread().getName() +" is at floor: "+ (i+1));
                    }

                    // update current floor to destination
                    currentFloor = serviceRequest.getDestinationFloor();
                    // complete request
                    scheduler.serviceRequest(serviceRequest, Id);

                    // *end test*
                    if(testEnabled)
                        break;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
