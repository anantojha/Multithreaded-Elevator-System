package Elevator.Global;

public interface StateMachine {
    void updateState();
	void updateState(boolean condition);
	String getCurrentState();
    String getPreviousState();
}
