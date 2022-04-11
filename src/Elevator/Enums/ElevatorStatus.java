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
		/*
		 * nextState() moves INITIALIZE to IDLE
		 * 
		 * input: none
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public ElevatorStatus nextState() {
			return IDLE;
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

		/*
		 * nextState() moves IDLE to FAULT_DETECTED if a fault is present or RUNNING otherwise
		 * 
		 * input: boolean
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public ElevatorStatus nextState(boolean isFault) {
			return isFault ? FAULT_DETECTED : RUNNING;
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

		/*
		 * nextState() moves RUNNING to ARRIVED
		 * 
		 * input: none
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public ElevatorStatus nextState() {
			return ARRIVED;
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

		/*
		 * nextState() moves ARRIVED to OPEN_DOOR
		 * 
		 * input: none
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public ElevatorStatus nextState() {
			return OPEN_DOOR;
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

		/*
		 * nextState() moves OPEN_DOOR to CLOSE_DOOR
		 * 
		 * input: none
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public ElevatorStatus nextState() {
			return CLOSE_DOOR;
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

		/*
		 * nextState() moves CLOSE_DOOR to IDLE if destination is reached and RUNNING otherwise
		 * 
		 * input: none
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public ElevatorStatus nextState(boolean isDestinationReached) {
			return isDestinationReached ? IDLE : RUNNING;
		}
	},
	/*
	 * FAULT_DETECTED represents the state an elevator takes when a fault is first detected.
	 */
	FAULT_DETECTED {
		@Override
		public String toString() {
			return "FAULT_DETECTED";
		}

		/*
		 * nextState() moves FAULT_DETECTED to RESETTING
		 * 
		 * input: none
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public ElevatorStatus nextState() {
			return RESETTING;
		}
	},
	/*
	 * RESETTING represents the state an elevator takes when a fault occurs and the elevator has to reset.
	 */
	RESETTING {
		@Override
		public String toString() {
			return "RESETTING";
		}

		/*
		 * nextState() moves RESETTING to RESUMING
		 * 
		 * input: none
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public ElevatorStatus nextState() {
			return RESUMING;
		}
	},
	/*
	 * RESUMING represents the state an elevator takes when a fault is resolved.
	 */
	RESUMING {
		@Override
		public String toString() {
			return "RESUMING";
		}

		/*
		 * nextState() moves RESUMING to RUNNING
		 * 
		 * input: none
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public ElevatorStatus nextState() {
			return RUNNING;
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
}