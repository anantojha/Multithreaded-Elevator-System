package Elevator.ElevatorSubsystem;

import Elevator.Enums.Direction;
import Elevator.Enums.ElevatorStatus;
import Elevator.FloorSubsystem.Request;
import GUI.ControlPanelGUI;

import java.io.IOException;
import java.util.*;

/*
 * The Elevator class is responsible for accessing the tasks queue and servicing outstanding requests.
 * 
 */
public class Elevator implements Runnable {

	int initialFloor;
	int sourceFloor;
	int destinationFloor;
	int id;
	private ElevatorContext elevatorContext;
	private ElevatorState state;
	private ControlPanelGUI gui;
	Queue<Request> jobs;

	/*
	 * A constructor for the Elevator class. Initializes the GUI and state.
	 *
	 * Input: id (String): The elevator id previously entered from askElevatorId()
	 * method
	 * 
	 * Output: none
	 *
	 */
	public Elevator(int id, Queue<Request> jobs, ControlPanelGUI gui) throws IOException {
		// All elevators start at Floor 1
		this.id = id;
		this.initialFloor = 1;
		this.sourceFloor = 1;
		this.destinationFloor = 1;
		this.elevatorContext = new ElevatorContext(initialFloor, null);
		this.state = new ElevatorState(id);
		this.jobs = jobs;
		this.gui = gui;
	}

	/*
	 * The run() method will attempt to access the tasks queue and poll() a request (if one is available). If a
	 * request is available, the request will be serviced by the elevator.
	 *
	 * Input: none
	 * Output: none
	 *
	 */
	@Override
	public void run() {
		state.updateState();
		gui.initialize(id, state.getCurrentState());
		gui.updateStatus(id, state.getCurrentState());

		while (true) {
			System.out.println();

			while (jobs.isEmpty()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			Request task = jobs.poll();
			service(task);
		}
	}

	/*
	 * move() method simulates the movement of the elevator from currentFloor to the target floor
	 * 
	 * Input: int targetFloor
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
				elevatorContext.setDirection(Direction.UP);
				// state.updateState();
			} else {
				elevatorContext.setDirection(Direction.DOWN);
				// state.updateState();
			}

			print("Moving " + elevatorContext.getDirection());
			// Move to the targetFloor

			for (int i = Math.abs(elevatorContext.getCurrentFloor() - targetFloor); i > 0; i--) {
				int x = isDirectionUp ? 1 : -1; // If direction UP, increment current floor, else decrement
				print("Arrived at floor: " + elevatorContext.getCurrentFloor());
				elevatorContext.setCurrentFloor(elevatorContext.getCurrentFloor() + x);
				Thread.sleep(1000);
				gui.updateFloor(id, elevatorContext.getCurrentFloor());
				gui.updateElevatorQueue(id, jobs);
				gui.updateTableData(id,
						state.getCurrentState(), 
						elevatorContext.getDirection().toString(), 
						elevatorContext.getCurrentFloor(), 
						sourceFloor, 
						destinationFloor);

			}
			print("Arrived at floor: " + elevatorContext.getCurrentFloor());

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * faultDetected() method simulates the detection of a fault which is then followed a resolution (resetting)
	 *
	 * Input: Request serviceRequest
	 * Output: none
	 *
	 */
	public void faultDetected(Request serviceRequest) {

		String fault = serviceRequest.getFaultType() == 'f' ? "Hard Fault" : "Transient Fault";

		try {

			if (fault.equals("Hard Fault")) {
				// If a fault has been detected, move to the ground floor
				move(1);

				System.out.print("Elevator is repairing.");
				// Repair the elevator
				for (int i = 0; i < 20; i++) {
					System.out.print(".");
					Thread.sleep(1000);
				}
				// After some period of time, the elevator is considered repaired
				System.out.println("Elevator is repaired.");
				Thread.sleep(1000);

				System.out.println("Resuming Elevator activity.\n");
				Thread.sleep(1000);
			} else {
				System.out.print("Retrying...");
				for (int i = 0; i < 10; i++) {
					System.out.print(".");
					Thread.sleep(1000);
				}
				System.out.println();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * service() method services a request. Logic for each state an elevator can be in when completing a task.
	 * Updates UI as the state transitions from one to another.
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

		try {

			boolean sourceFLoorReached = false;
			boolean destinationFLoorReached = false;

			print("Started servicing " + serviceRequest);
			this.destinationFloor = serviceRequest.getDestinationFloor();
			this.sourceFloor = serviceRequest.getSourceFloor();

			while ((!sourceFLoorReached || !destinationFLoorReached)
					|| state.getCurrentState() != ElevatorStatus.IDLE.toString()) {

				switch (state.getCurrentState()) {

				// Handle IDLE state
				case "IDLE":
					state.updateState(serviceRequest.getFault());
					gui.updateStatus(id, state.getCurrentState());
					gui.updateElevatorQueue(id, jobs);
					break;

				// Handle RUNNING state
				case "RUNNING":
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
					state.updateState();
					gui.updateStatus(id, state.getCurrentState());
					gui.updateElevatorQueue(id, jobs);
					break;

				// Handle ARRIVED state
				case "ARRIVED":
					Thread.sleep(600);
					state.updateState();
					gui.updateStatus(id, state.getCurrentState());
					gui.updateElevatorQueue(id, jobs);
					break;

				// Handle OPEN_DOOR state
				case "OPEN_DOOR":
					print("Opening doors");
					Thread.sleep(600);
					print(destinationFLoorReached ? "Drop off passengers" : "Pick up passengers");
					state.updateState();
					gui.updateStatus(id, state.getCurrentState());
					gui.updateElevatorQueue(id, jobs);
					break;

				// Handle CLOSE_DOOR state
				case "CLOSE_DOOR":
					print("Closing doors");
					Thread.sleep(1400);

					state.updateState(destinationFLoorReached);
					gui.updateStatus(id, state.getCurrentState());
					gui.updateElevatorQueue(id, jobs);
					break;

				// Handle FAULT_DETECTED state
				case "FAULT_DETECTED":
					String fault = serviceRequest.getFaultType() == 'f' ? "Hard Fault" : "Transient Fault";
					print(fault + " has been detected");
					faultDetected(serviceRequest);
					Thread.sleep(2000);
					state.updateState();
					gui.updateStatus(id, state.getCurrentState());
					gui.updateElevatorQueue(id, jobs);
					break;

				// Handle RESETTING state
				case "RESETTING":
					// reset elevator
					print("Resetting");
					Thread.sleep(2000);
					//move(1);
					state.updateState();
					gui.updateStatus(id, state.getCurrentState());
					gui.updateElevatorQueue(id, jobs);
					break;

				// Handle RESUMING state
				case "RESUMING":
					print("Resuming");
					Thread.sleep(3000);
					state.updateState();
					gui.updateStatus(id, state.getCurrentState());
					gui.updateElevatorQueue(id, jobs);
					break;

				// Handle TERMINATE state
				case "TERMINATE":
					break;

				default:
					break;
				}
				//Update GUI 
				gui.updateElevatorQueue(id, jobs);
				gui.updateTableData(id,
						state.getCurrentState(), 
						"", 
						elevatorContext.getCurrentFloor(), 
						null, 
						null);
			}
			print("Waiting for next request...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * The getJobs() is a getter method for retrieving the queue of requests the
	 * elevator will receive from the scheduler.
	 * 
	 * Input: None Output: Queue of requests received from scheduler
	 */
	public Queue<Request> getJobs() {
		return jobs;
	}
	
	/*
	 * The print() method prints a structured output string to console.
	 * 
	 * Input: string (String): the string to be printed Output: None
	 */
	private void print(String string) {
		System.out.println("[ " + Thread.currentThread().getName() + " ]: " + string);
	}
}