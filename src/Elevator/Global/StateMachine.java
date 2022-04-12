package Elevator.Global;
/*
 * Interface for the state machine.
 */
public interface StateMachine {
    void updateState();
	void updateState(boolean condition);
	String getCurrentState();
}
