package Elevator.Global;

import Elevator.Enums.FloorStatus;

public interface StateMachine {

    void updateState();
    String getCurrentState();
    String getPreviousState();

}
