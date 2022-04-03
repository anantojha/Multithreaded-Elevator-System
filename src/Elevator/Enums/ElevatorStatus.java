package Elevator.Enums;

/*
 * ElevatorStatus is a collection of constants representing the states an elevator can take.
 * 
 */
public enum ElevatorStatus {
	/*
	 * INITIALIZE represents the state an elevator when it starts up.
	 */
	INITIALIZE {
		@Override
		public String toString() {
			return "INITIALIZE";
		}

		@Override
		public ElevatorStatus nextState() {
			ElevatorStatus nextState = IDLE;
			printUpdate(nextState);
			return nextState;
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

		@Override
		public ElevatorStatus nextState() {
			ElevatorStatus nextState = RUNNING;
			printUpdate(nextState);
			return nextState;
		}
	},
	/*
	 * RUNNING represents the state an elevator takes when currently transporting a
	 * customer across floors.
	 */
	RUNNING {
		@Override
		public String toString() {
			return "RUNNING";
		}

		@Override
		public ElevatorStatus nextState() {
			ElevatorStatus nextState = ARRIVED;
			printUpdate(nextState);
			return nextState;
		}
	},
	/*
	 * ARRIVED represents the state an elevator takes when the elevator arrives at
	 * the destination floor.
	 */
	ARRIVED {
		@Override
		public String toString() {
			return "ARRIVED";
		}

		@Override
		public ElevatorStatus nextState() {
			ElevatorStatus nextState = OPEN_DOOR;
			printUpdate(nextState);
			return nextState;
		}
	},

	/*
	 * OPEN_DOOR represents the state an elevator takes when opening the door.
	 */
	OPEN_DOOR {
		@Override
		public String toString() {
			return "OPEN_DOOR";
		}

		@Override
		public ElevatorStatus nextState() {
			ElevatorStatus nextState = CLOSE_DOOR;
			printUpdate(nextState);
			return nextState;
		}
	},
	/*
	 * CLOSE_DOOR represents the state an elevator takes when closing the door.
	 */
	CLOSE_DOOR {
		@Override
		public String toString() {
			return "CLOSE_DOOR";
		}

		@Override
		public ElevatorStatus nextState(boolean isDestinationReached) {
			ElevatorStatus nextState = isDestinationReached ? IDLE : RUNNING;
			printUpdate(nextState);
			return nextState;
		}
	},
	/*
	 * TERMINATE represents the state an elevator takes when the elevator stops
	 * working.
	 */
	TERMINATE {
		public String toString() {
			return "TERMINATE";
		}
	};

	public ElevatorStatus nextState() {
		// TODO Auto-generated method stub
		return null;
	}

	public ElevatorStatus nextState(boolean condition) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void printUpdate(ElevatorStatus toState) {
		System.out.println("[ State Machine ]: Updating state to " + toState.toString());
	}
}
