package Elevator.ElevatorSubsystem;

import Elevator.Enums.StateMachineStatus;

public class StateMachineState {

	private StateMachineStatus state;

	public StateMachineState() {
		state = null;
	}

	public StateMachineState(StateMachineStatus state) {
		this.state = state;
	}

	public void setState(StateMachineStatus state) {
		this.state = state;
	}

	public String getState() {
		return state.toString();
	}

}
