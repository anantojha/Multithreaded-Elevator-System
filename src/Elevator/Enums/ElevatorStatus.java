package Elevator.Enums;
/*
 * ElevatorStatus is a collection of constants representing the states an elevator can take.
 * 
 */
public enum ElevatorStatus {
	/*
	 * RUNNING represents the state an elevator takes when currently transporting a customer across floors.
	 */
    RUNNING {
        @Override
        public String toString() {
            return "RUNNING";
        }
    },
	/*
	 * ARRIVED represents the state an elevator takes when the elevator arrives at the destination floor.
	 */
    ARRIVED {
        @Override
        public String toString() {
            return "ARRIVED";
        }
    },
	/*
	 * IDLE represents the state an elevator takes when it is currently inactive.
	 */
    IDLE {
        @Override
        public String toString() {
            return "IDLE";
        }
    },
	/*
	 * CLOSE_DOOR represents the state an elevator takes when closing the door.
	 */
    OPEN_DOOR {
        @Override
        public String toString() {
            return "OPEN DOOR";
        }
    },
	/*
	 * OPEN_DOOR represents the state an elevator takes when opening the door.
	 */
    CLOSE_DOOR {
        @Override
        public String toString() {
            return "CLOSE DOOR";
        }
    },
	/*
	 * TERMINATE represents the state an elevator takes when the elevator stops working.
	 */
    TERMINATE {
    	public String toString() {
    		return "TERMINATE";
    	}
    }
}
