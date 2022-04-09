package Elevator.Global;

import Elevator.Enums.ElevatorStatus;
import Elevator.Enums.FloorStatus;

public interface StateMachine {

    void updateState();
	void updateState(boolean condition);
	String getCurrentState();
    String getPreviousState();
}
