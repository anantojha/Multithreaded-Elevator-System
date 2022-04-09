package Elevator.ElevatorSubsystem;

import Elevator.Enums.Direction;
import Elevator.Enums.ElevatorStatus;
import Elevator.FloorSubsystem.Request;
import GUI.ControlPanelGUI;

import java.io.IOException;
import java.util.*;

/*
 * The Elevator class represents the consumer side of the algorithm. It is responsible for accessing the requests sent to the scheduler
 * and fulfilling them given that the correct conditions are met.
 * 
 */
public class Elevator implements Runnable {

	int initialFloor;
	int id;
	private ElevatorContext elevatorContext;
	private ElevatorState state;
	private ControlPanelGUI gui;
	Queue<Request> jobs;

	/*
	 * A constructor for the Elevator class. The constructor initializes the shared
	 * data structure and sets the id of the Elevator. Each elevator starts from
	 * floor 1.
	 *
	 * Input: id (String): The elevator id previously entered from askElevatorId()
	 * method
	 * 
	 * Output: none
	 *
	 */
	public Elevator(int id, Queue<Request> jobs) throws IOException {
		// All elevators start at Floor 1
		this.id = id;
		this.initialFloor = 1;
		this.elevatorContext = new ElevatorContext(initialFloor, null, ElevatorStatus.INITIALIZE);
		this.state = new ElevatorState(elevatorContext);
		this.jobs = jobs;
	}

	/*
	 * The getCurrentStatus() method returns the status the elevator is currently
	 * in.
	 * 
	 * Input: none Output: Return elevator status as Enum ElevatorStatus.
	 * 
	 */
	public ElevatorStatus getCurrentStatus() {
		return elevatorContext.getStatus();
	}

	/*
	 * The updateState() method updates the current state of the elevator
	 * 
	 */
	public void updateState() {
		ElevatorStatus currentState = elevatorContext.getStatus();
		currentState = currentState.nextState();
		elevatorContext.setStatus(currentState);
	}

	/*
	 * The run() method is the primary sequence that is run when a thread is active.
	 * In this case, the Elevator class will attempt to send requests to the
	 * scheduler for a Task and then receive a response from the scheduler.
	 *
	 * Input: none Output: none
	 *
	 */
	@Override
	public void run() {
		try {
			gui = new ControlPanelGUI(id);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		updateState();

		while (true) {
			System.out.println();

			while (jobs.isEmpty()) {
			}

			Request task = jobs.poll();
			service(task);
		}
	}

	/*
	 * move(int targetFloor) method moves elevator from currentFloor to the inputed
	 * floor
	 * 
	 * Input: floor int (serviceRequest.getDestinationFloor() or
	 * serviceRequest.getSourceFloor()): Floor number that the elevator will moving
	 * to.
	 * 
	 * Output: none
	 * 
	 */
	private void move(int targetFloor) {
		try {
			
			// Check if elevator is already at target floor
			if (elevatorContext.getCurrentFloor() == targetFloor) {
				return;
			}

			// Determine the direction the elevator will need to move
			boolean isDirectionUp = (elevatorContext.getCurrentFloor() < targetFloor);

			// Set the direction state based on direction boolean above
			if (isDirectionUp) {
				state.setElevatorContext(elevatorContext.setDirection(Direction.UP));
				// state.updateState();
			} else {
				state.setElevatorContext(elevatorContext.setDirection(Direction.DOWN));
				// state.updateState();
			}

			print("Moving " + elevatorContext.getDirection());
			// Move to the targetFloor
//			state.setElevatorContext(elevatorContext.setStatus(ElevatorStatus.RUNNING));
//			state.updateState();
			for (int i = Math.abs(elevatorContext.getCurrentFloor() - targetFloor); i > 0; i--) {
				int x = isDirectionUp ? 1 : -1; // If direction UP, increment current floor, else decrement
				print("Arrived at floor: " + elevatorContext.getCurrentFloor());
				state.setElevatorContext(elevatorContext.setCurrentFloor(elevatorContext.getCurrentFloor() + x));
//               state.updateState();
				Thread.sleep(1000);
				gui.moveElevator(elevatorContext.getCurrentFloor());
				gui.updateElevatorQueue(jobs);
			}
			print("Arrived at floor: " + elevatorContext.getCurrentFloor());


		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void updateGUI(Request serviceRequest) throws InterruptedException {
		gui.updateElevatorLabels(elevatorContext.getStatus());
		gui.updateCurrentRequestLabel(serviceRequest);
		gui.updateElevatorQueue(jobs);
	}


	public void faultDetected(Request serviceRequest){
		// String fault = type == 'f' ? "Hard Fault" : "Transient Fault";

		try {
			print("Fault has been detected");
			state.setElevatorContext(elevatorContext.setStatus(ElevatorStatus.FAULT_DETECTED));
			updateGUI(serviceRequest);
			Thread.sleep(2000);

			// reset elevator
			print("Resetting");
			updateState();
			updateGUI(serviceRequest);
			Thread.sleep(2000);
			move(1);

			print("Resuming");
			updateState();
			updateGUI(serviceRequest);
			Thread.sleep(3000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * service(Request serviceRequest) method services the request received from the
	 * socket
	 * 
	 * Input: serviceRequest(Request): Request that the elevator will be completing.
	 * 
	 * Output: none
	 * 
	 */
	private void service(Request serviceRequest) {
		if (serviceRequest == null) {
			return;
		}

		if (serviceRequest.getFault()) {
			faultDetected(serviceRequest);
		}

		try {

			boolean sourceFLoorReached = false;
			boolean destinationFLoorReached = false;

			print("Started servicing " + serviceRequest);

			while ((!sourceFLoorReached || !destinationFLoorReached)
					|| elevatorContext.getStatus() != ElevatorStatus.IDLE) {
				switch (elevatorContext.getStatus()) {

				// Handle IDLE state
				case IDLE:
					updateGUI(serviceRequest);
					updateState();
					break;

				// Handle RUNNING state
				case RUNNING:
					updateGUI(serviceRequest);
					if (!sourceFLoorReached) {
						// Move to source floor
						move(serviceRequest.getSourceFloor());
						sourceFLoorReached = true;
					} else {
						// Move to destination floor
						move(serviceRequest.getDestinationFloor());
						sourceFLoorReached = true;
						destinationFLoorReached = true;
					}
					updateState();
					updateGUI(serviceRequest);
					break;

				// Handle ARRIVED state
				case ARRIVED:
					updateGUI(serviceRequest);
					Thread.sleep(600);
					updateState();
					break;

				// Handle OPEN_DOOR state
				case OPEN_DOOR:
					updateGUI(serviceRequest);
					print("Opening doors");
					Thread.sleep(600);
					print(destinationFLoorReached ? "Drop off passengers" : "Pick up passengers");
					updateState();
					break;

				// Handle CLOSE_DOOR state
				case CLOSE_DOOR:
					updateGUI(serviceRequest);
					print("Closing doors");
					Thread.sleep(1400);

					elevatorContext.setStatus(elevatorContext.getStatus().nextState(destinationFLoorReached));
					break;

				// Handle IDLE state
				case RESUMING:
					updateState();
					updateGUI(serviceRequest);
					break;

				// Handle TERMINATE state
				case TERMINATE:
					break;

				default:
					break;
				}

			}
			print("Waiting for next request...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * The getJobs() is a getter method for retrieving the queue of requests 
	 * the elevator will receive from the scheduler.
	 * 
	 * Input: Nonw
	 * Output: Queue of requests received from scheduler
	 */
	public Queue<Request> getJobs(){
		return jobs;
	}
	/*
	 * The print() method prints a structured output string to console.
	 * 
	 * Input: string (String): the string to be printed
	 * 
	 * Output: Return elevator status as Enum ElevatorStatus.
	 * 
	 */
	private void print(String string) {
		System.out.println("[ " + Thread.currentThread().getName() + " ]: " + string);
	}
}