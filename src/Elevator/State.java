package Elevator;
/*
 * State is a collection of constants representing the different states the state machine can take 
 * 
 * 
 */
public enum State {
	INITIALIZE,
	RUNNING,
	CLOSING_DOORS,
	OPENING_DOORS,
	OPEN_DOOR,
	CLOSE_DOOR,
	IDLE
}
