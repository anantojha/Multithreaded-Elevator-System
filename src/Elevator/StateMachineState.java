package Elevator;

import Elevator.Enums.StateMachineStatus;
/*
 * StateMachineState represents the State class of the State Machine
 * 
 */
public class StateMachineState {

	private StateMachineStatus state;

	/*
	 * StateMachineState() is a constructor for the StateMachineState class. The constructor initializes the current state to null.
	 * 
	 * Input: none
	 * Output: none
	 * 
	 */
	public StateMachineState() {
		state = null;
	}

	/*
	 * StateMachineState(StateMachineStatus) is a constructor for the StateMachineState class. The constructor initializes the current state to state.
	 * 
	 * Input: StateMachineStatus state
	 * Output: none
	 * 
	 */
	public StateMachineState(StateMachineStatus state) {
		this.state = state;
	}

	/*
	 * setState(StateMachineStatus) sets the current state equal to state.
	 * 
	 * Input: StateMachineStatus state
	 * Output: none
	 * 
	 */
	public void setState(StateMachineStatus state) {
		this.state = state;
	}
	/*
	 * getState() gets the current state.
	 * 
	 * Input: none
	 * Output: StateMachineStatus state
	 * 
	 */
	public String getState() {
		return state.toString();
	}

}
