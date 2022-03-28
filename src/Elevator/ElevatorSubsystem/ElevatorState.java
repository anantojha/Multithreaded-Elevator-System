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


	private ElevatorContext state;
	private ElevatorContext previousState;
	private ElevatorContext nextState;

	public ElevatorState(ElevatorContext initialContext) {
		this.state = initialContext;
		this.previousState = null;
		this.nextState = null;
	}

	public ElevatorState setElevatorContext(ElevatorContext context){
		nextState = context;
		return this;
	}

	@Override
	public void updateState() {
		ElevatorContext temp  = state;
		this.state = nextState;
		previousState = temp;
		// System.out.println(state.toString());
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
