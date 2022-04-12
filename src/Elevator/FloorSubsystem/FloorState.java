package Elevator.FloorSubsystem;

import Elevator.Enums.FloorStatus;
import Elevator.Global.StateMachine;

/*
 * FloorState represents the State class of the Floor
 * 
 */
public class FloorState implements StateMachine {
	private int floorId;
	private FloorStatus state;
	/*
	 * FloorState() is the constructor of the FloorState class. Default start state for floor -> Initialize.
	 * 
	 * Input: none
	 * Output: none
	 * 
	 */
	public FloorState(int id) {
		this.floorId = id;
		this.state = FloorStatus.INITIALIZE;
	}

	/*
	 * updateState() sets the current state based on prior state.
	 * 
	 * Input: none
	 * Output: none
	 * 
	 */
	@Override
	public void updateState() {
		this.state = state.nextState();
		System.out.println("[ Floor " + floorId + " state machine ]: Updated state to: " + state);
	}

	/*
	 * getState() gets the current state.
	 * 
	 * Input: none
	 * Output: FloorStatus state
	 * 
	 */
	@Override
	public String getCurrentState() {
		return state.toString();
	}

	/*
	 * updateState(boolean) updates the state.
	 *
	 * Input: boolean
	 * Output: none
	 *
	 */
	@Override
	public void updateState(boolean condition) {
		updateState();
	}
}
