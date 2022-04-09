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

	private ElevatorStatus state;
	private ElevatorStatus previousState;

	public ElevatorState() {
		this.state = ElevatorStatus.INITIALIZE;
		this.previousState = null;
	}

	@Override
	public void updateState() {
		previousState = state;
		this.state = state.nextState();
	}
	
	@Override
	public void updateState(boolean condition) {
		previousState = state;
		this.state = state.nextState(condition);
	}

	@Override
	public String getCurrentState() {
		return state.toString();
	}

	@Override
	public String getPreviousState() {
		return previousState.toString();
	}
}
