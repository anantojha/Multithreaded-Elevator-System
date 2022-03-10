package Elevator.FloorSubsystem;

import Elevator.Enums.FloorStatus;
/*
 * FloorState represents the State class of the Floor
 * 
 */
public class FloorState {
	private FloorStatus state;
	/*
	 * FloorState() is the constructor of the FloorState class. It initializes the current state equal to null.
	 * 
	 * Input: none
	 * Output: none
	 * 
	 */
	public FloorState() {
		state = null;
	}
	/*
	 * FloorState(FloorStatus) is the constructor of the FloorState class. It initializes the current state equal to the parameter.
	 * 
	 * Input: FloorStatus state
	 * Output: none
	 * 
	 */
	public FloorState(FloorStatus state) {
		this.state = state;
	}
	/*
	 * setState(FloorStatus) sets the current state equal to the parameter.
	 * 
	 * Input: FloorStatus state
	 * Output: none
	 * 
	 */
	public void setState(FloorStatus state) {
		this.state = state;
	}
	/*
	 * getState() gets the current state.
	 * 
	 * Input: none
	 * Output: FloorStatus state
	 * 
	 */
	public String getState() {
		return state.toString();
	}
}
