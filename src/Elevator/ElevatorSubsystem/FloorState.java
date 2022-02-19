package Elevator.ElevatorSubsystem;

import Elevator.Enums.FloorStatus;

public class FloorState {
	private FloorStatus state;
	
	public FloorState() {
		state = null;
	}
	public FloorState(FloorStatus state) {
		this.state = state;
	}

	public void setState(FloorStatus state) {
		this.state = state;
	}

	public FloorStatus getState() {
		return state;
	}
}
