package Elevator.ElevatorSubsystem;

import Elevator.Enums.Direction;
import Elevator.Enums.ElevatorStatus;
import Elevator.FloorSubsystem.Request;

import java.io.IOException;
import java.util.*;

/*
 * The Elevator class represents the consumer side of the algorithm. It is responsible for accessing the requests sent to the scheduler
 * and fulfilling them given that the correct conditions are met.
 * 
 */
public class Elevator implements Runnable {

    int initialFloor;
    private ElevatorContext elevatorContext;
    private ElevatorState state;
    private Timer timer;
    boolean forceFault = false;
    final private float avgtime = 1.347887712f;
    Queue<Request> jobs;

    /*
     * A constructor for the Elevator class. The constructor initializes the shared data structure and sets the id
     * of the Elevator. Each elevator starts from floor 1.
     *
     * Input:
     * id (String): The elevator id previously entered from askElevatorId() method
     * 
     * Output: none
     *
     */
    public Elevator(Queue<Request> jobs) throws IOException {
    	//All elevators start at Floor 1
        this.initialFloor = 1;
        this.elevatorContext = new ElevatorContext(initialFloor, null, ElevatorStatus.INITIALIZE);
        this.state = new ElevatorState(elevatorContext);
        this.jobs = jobs;
    }

    /*
     * The getCurrentStatus() method returns the status the elevator is currently in.
     * 
     * Input: none
     * Output: Return elevator status as Enum ElevatorStatus.
     * 
     */
    public ElevatorStatus getCurrentStatus() {
        return elevatorContext.getStatus();
    }

    /*
     * The run() method is the primary sequence that is run when a thread is active. In this case, the Elevator class will
     * attempt to send requests to the scheduler for a Task and then receive a response from the scheduler.
     *
     * Input: none
     * Output: none
     *
     */
    @Override
    public void run() {

        while (true) {
            state.setElevatorContext(elevatorContext.setStatus(ElevatorStatus.IDLE));
            state.updateState();

            while (jobs.isEmpty()){}

            Request task = jobs.poll();
            service(task);
        }
    }

    /*
     * move(int targetFloor) method moves elevator from currentFloor to the inputed floor
     * 
     * Input: 
     * floor int (serviceRequest.getDestinationFloor() or serviceRequest.getSourceFloor()): Floor number that the elevator will moving to.
     * 
     * Output: none
     * 
     */
    private void move(int targetFloor) {
    	try {

    		//Auxillary code for iteration 5
    		if(forceFault) {
	    		int initialFloor = elevatorContext.getCurrentFloor();
	    		//Create a timer
	    		timer = new Timer();
	    		timer.schedule(new TimerTask() {
	    			public void run() {
	    				//When the timer ends, and the elevator is not at their target floor, exit. Otherwise, continue.
	    				if(elevatorContext.getCurrentFloor() == targetFloor) {
	    					return;
	    				}else {
	    					System.out.println("Fault has been detected | Timer was " + (Math.abs(initialFloor - targetFloor) * avgtime) + 1600/1000);
	    					System.exit(1);
	    				}
	    			}
	    		}, (long) (Math.abs(initialFloor - targetFloor) * avgtime * 1000));

	    		Thread.sleep(1000);


    		}
            // Check if elevator is already at target floor
            if (elevatorContext.getCurrentFloor() == targetFloor){
                return;
            }

    		// Determine the direction the elevator will need to move 
        	boolean isDirectionUp = (elevatorContext.getCurrentFloor() < targetFloor);

        	// Set the direction state based on direction boolean above
        	if (isDirectionUp) {
                state.setElevatorContext(elevatorContext.setDirection(Direction.UP));
                state.updateState();
        	} else {
                state.setElevatorContext(elevatorContext.setDirection(Direction.DOWN));
                state.updateState();
        	}

        	// Move to the targetFloor
            state.setElevatorContext(elevatorContext.setStatus(ElevatorStatus.RUNNING));
            state.updateState();
        	for (int i = Math.abs(elevatorContext.getCurrentFloor() - targetFloor); i > 0; i--) {
        		int x = isDirectionUp? 1 : -1; 	// If direction UP, increment current floor, else decrement
        		System.out.println(Thread.currentThread().getName() + " is at floor: " + elevatorContext.getCurrentFloor());
                state.setElevatorContext(elevatorContext.setCurrentFloor(elevatorContext.getCurrentFloor() + x));
                state.updateState();
        		Thread.sleep(1000);
        	}
        	System.out.println(Thread.currentThread().getName() + " is at floor: " + elevatorContext.getCurrentFloor());

        	// Update state when approaching targetFloor
            state.setElevatorContext(elevatorContext.setStatus(ElevatorStatus.ARRIVED));
            state.updateState();
            Thread.sleep(600);
            state.setElevatorContext(elevatorContext.setStatus(ElevatorStatus.OPEN_DOOR));
            state.updateState();
            Thread.sleep(600);

       	} catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * service(Request serviceRequest) method services the request received from the socket
     * 
     * Input: 
     * serviceRequest(Request): Request that the elevator will be completing.
     * 
     * Output: none
     * 
     */
    private void service(Request serviceRequest) {
        if (serviceRequest == null){
            return;
        }

    	try {
            System.out.println();
	        System.out.println(Thread.currentThread().getName() +" is servicing "+ serviceRequest);

	        // Move to source floor
	        move(serviceRequest.getSourceFloor());
	        System.out.println(Thread.currentThread().getName() + " Pick up passengers");
            state.setElevatorContext(elevatorContext.setStatus(ElevatorStatus.CLOSE_DOOR));
            state.updateState();
	        Thread.sleep(1400);

	        // Move to destination floor
	        move(serviceRequest.getDestinationFloor());
	        System.out.println(Thread.currentThread().getName() + " Drop off passengers" );
            state.setElevatorContext(elevatorContext.setStatus(ElevatorStatus.CLOSE_DOOR));
            state.updateState();
	        Thread.sleep(1400);

    	} catch (InterruptedException e) {
            e.printStackTrace();
        }   
    }
}