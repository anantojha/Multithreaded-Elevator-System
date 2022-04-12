package Elevator.ElevatorSubsystem;

import Elevator.Enums.Direction;
import Elevator.Enums.ElevatorStatus;
import Elevator.Enums.FloorStatus;
import Elevator.Global.StateMachine;
import Elevator.Global.SystemConfiguration;

import java.util.Observable;

/*
 * ElevatorState represents the State class of the Elevator
 * 
 */
public class ElevatorState implements StateMachine {

	private int elevatorId;
	private ElevatorStatus state;

	public ElevatorState(int id) {
		this.elevatorId = id;
		this.state = ElevatorStatus.INITIALIZE;
	}

	/*
	 * Updates the state to the next state in the state machine
	 */
	@Override
	public void updateState() {
		this.state = state.nextState();
		System.out.println("[ Elevator " + elevatorId + " State Machine ]: Updating state to " + state.toString());
	}
	
	/*
	 * Updates the state to the next state in the state machine based on a condition.
	 */
	@Override
	public void updateState(boolean condition) {
		this.state = state.nextState(condition);
	}

	/*
	 * Returns the current state.
	 * 
	 * Output (String) current state
	 */
	@Override
	public String getCurrentState() {
		return state.toString();
	}
}
