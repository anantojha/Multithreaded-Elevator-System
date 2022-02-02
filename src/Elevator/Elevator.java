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
                // get request from table
                Request serviceRequest = scheduler.getRequest();

                // validate if source floor of request is the thread's floor
                if (serviceRequest.getSourceFloor() == currentFloor) {
                    System.out.println(Thread.currentThread().getName() +" is servicing "+ serviceRequest);
                    try {
                        for(int i = 0; i <= (serviceRequest.getDestinationFloor() - serviceRequest.getSourceFloor()); i++){
                            Thread.sleep(1000);
                            System.out.println(Thread.currentThread().getName() +" is at floor: "+ (i+1));
                        }
                        currentFloor = serviceRequest.getDestinationFloor();
                        scheduler.serviceRequest(serviceRequest, Id);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() +" is servicing "+ serviceRequest);
                    try {
                        System.out.println(Thread.currentThread().getName() +" is at floor: "+ currentFloor);

                        if(currentFloor > serviceRequest.getSourceFloor()){
                            for(int i = Math.abs(serviceRequest.getSourceFloor() - currentFloor); i >= 1 ; i--){
                                Thread.sleep(1000);
                                System.out.println(Thread.currentThread().getName() +" is at floor: "+ (i));
                            }
                        }


                        for(int i = 0; i <= Math.abs(serviceRequest.getSourceFloor() - currentFloor); i++){
                            Thread.sleep(1000);
                            System.out.println(Thread.currentThread().getName() +" is at floor: "+ (i+1));
                        }
                        currentFloor = serviceRequest.getSourceFloor();
                        currentFloor = serviceRequest.getDestinationFloor();
                        scheduler.serviceRequest(serviceRequest, Id);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
            }
        }
    }
}
