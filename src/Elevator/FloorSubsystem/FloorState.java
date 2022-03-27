package Elevator.FloorSubsystem;

import Elevator.Enums.FloorStatus;

/*
 * FloorState represents the State class of the Floor
 * 
 */
public class FloorState {
	private FloorStatus state;
	private FloorStatus previousState;
	/*
	 * FloorState() is the constructor of the FloorState class. Default start state for floor -> Initialize.
	 * 
	 * Input: none
	 * Output: none
	 * 
	 */
	public FloorState() {
		this.state = FloorStatus.INITIALIZE;
		this.previousState = null;
	}

	/*
	 * updateState() sets the current state based on prior state.
	 * 
	 * Input: none
	 * Output: none
	 * 
	 */
	public FloorStatus updateState() {
		state = determineNextState();
		return state;
	}

	/*
	 * determineNextState() sets the current state based on prior state.
	 *
	 * Input: none
	 * Output: FloorStatus for the next state
	 *
	 */
	public FloorStatus determineNextState(){
		this.previousState = state;
		if (previousState == FloorStatus.INITIALIZE){
			return FloorStatus.PROCESSING;
		} else if(previousState == FloorStatus.PROCESSING) {
			return FloorStatus.WAITING;
		} else if(previousState == FloorStatus.WAITING) {
			return FloorStatus.SENDING;
		} else if(previousState == FloorStatus.SENDING) {
			return FloorStatus.RECEIVING;
		} else if(previousState == FloorStatus.RECEIVING) {
			return FloorStatus.WAITING;
		}
		return FloorStatus.ERROR;
	}

	/*
	 * getState() gets the current state.
	 * 
	 * Input: none
	 * Output: FloorStatus state
	 * 
	 */
	public String getCurrentState() {
		return state.toString();
	}

	/*
	 * getPreviousState() gets the previous state.
	 *
	 * Input: none
	 * Output: FloorStatus previousState
	 *
	 */
	public String getPreviousState() {
		return previousState.toString();
	}
}
