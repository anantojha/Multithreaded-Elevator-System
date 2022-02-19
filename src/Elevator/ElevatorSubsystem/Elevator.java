package Elevator.ElevatorSubsystem;
import Elevator.Enums.Direction;
import Elevator.Enums.ElevatorStatus;
import Elevator.SchedulerSubsystem.Scheduler;
import Elevator.FloorSubsystem.Request;

/*
 * The Elevator class represents the consumer side of the algorithm. It is responsible for accessing the requests sent to the scheduler
 * and fulfilling them given that the correct conditions are met.
 * 
 */
public class Elevator implements Runnable{

    private Scheduler scheduler;
    private int Id;
    private boolean testEnabled = false;
    private ElevatorState state;

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
        this.state = new ElevatorState(1);
        scheduler.addElevatorState(this.state);
    }

    public int getId() {
        return Id;
    }

    public Integer getCurrentFloor() {
        return state.getCurrentFloor();
    }

    public ElevatorStatus getCurrentStatus() {
        return state.getStatus();
    }

    public void updateState(ElevatorStatus status) {
        state.setStatus(status);
        if (status.equals(ElevatorStatus.RUNNING)){
            System.out.println("Elevator " + getId() + " " + getCurrentStatus() + " | " + state.getDirection());
        } else {
            System.out.println("Elevator " + getId() + " " + getCurrentStatus());
        }
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

            updateState(ElevatorStatus.IDLE);
            System.out.println();

            // synchronize with scheduler
            synchronized (scheduler) {
                // get request from scheduler
                Request serviceRequest = scheduler.getRequest();
                
                // elevator is servicing request
                System.out.println(Thread.currentThread().getName() +" is servicing "+ serviceRequest);

                // Move to source floor if not already on source floor
                if (serviceRequest.getSourceFloor() != state.getCurrentFloor()) {
                    state.setDirection(Direction.DOWN);
                    try {
                        // go to source floor
                        if(state.getCurrentFloor() > serviceRequest.getSourceFloor()){

                            updateState(ElevatorStatus.RUNNING);

                            for(int i = Math.abs(serviceRequest.getSourceFloor() - state.getCurrentFloor()); i >= 1 ; i--){
                                Thread.sleep(1000);
                                System.out.println(Thread.currentThread().getName() +" is at floor: "+ (i+1));
                            }
                            // update current floor to source
                            state.setCurrentFloor(serviceRequest.getSourceFloor());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
              
                try {
                    // go from source floor to destination floor
                    for(int i = 0; i <= (serviceRequest.getDestinationFloor() - serviceRequest.getSourceFloor()); i++) {
                        state.setDirection(Direction.UP);
                        if ((i + 1) == 1) {
                            System.out.println(Thread.currentThread().getName() + " is at floor: " + (i + 1) + " - pickup");
                            Thread.sleep(1000);
                            updateState(ElevatorStatus.ARRIVED);
                            Thread.sleep(600);
                            updateState(ElevatorStatus.OPEN_DOOR);
                            Thread.sleep(600);
                            updateState(ElevatorStatus.CLOSE_DOOR);
                            updateState(ElevatorStatus.RUNNING);
                            Thread.sleep(1400);
                        } else if ((i + 1) == serviceRequest.getDestinationFloor()) {
                            System.out.println(Thread.currentThread().getName() + " is at floor: " + (i + 1) + " - dropoff");
                            Thread.sleep(1000);
                            updateState(ElevatorStatus.ARRIVED);
                            Thread.sleep(600);
                            updateState(ElevatorStatus.OPEN_DOOR);
                            Thread.sleep(600);
                            updateState(ElevatorStatus.CLOSE_DOOR);
                            Thread.sleep(1400);
                        } else {
                            Thread.sleep(1000);
                            System.out.println(Thread.currentThread().getName() + " is at floor: " + (i + 1));
                        }
                    }

                    // update current floor to destination
                    state.setCurrentFloor(serviceRequest.getDestinationFloor());

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
