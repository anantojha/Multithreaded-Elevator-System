package Elevator.Enums;

/*
 * FloorStatus is a collection of constants representing the states an elevator can take.
 * 
 */
public enum FloorStatus {
	/*
	 * INITIALIZE represents the state a floor takes when currently initializing its variables.
	 */
	INITIALIZE {
		public String toString() {
			return "INITIALIZE";
		}
		
		/*
		 * nextState() moves INITALIZE to PROCESSING
		 * 
		 * input: none
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public FloorStatus nextState() {
			return PROCESSING;
		}
	},
	/*
	 * PROCESSING represents the state a floor takes when processing the CSV file of requests.
	 */
	PROCESSING {
		public String toString() {
			return "PROCESSING";
		}
		
		/*
		 * nextState() moves PROCESSING to WAITING
		 * 
		 * input: none
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public FloorStatus nextState() {
			return WAITING;
		}
	},
	/*
	 * SENDING represents the state a floor takes when sending a request to the Scheduler.
	 */
	SENDING {
		public String toString() {
			return "SENDING";
		}
		
		/*
		 * nextState() moves SENDING to RECEIVING
		 * 
		 * input: none
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public FloorStatus nextState() {
			return RECEIVING;
		}
	},
	/*
	 * RECEIVING represents the state a floor takes when receiving a response from the Scheduler.
	 */
	RECEIVING {
		public String toString() {
			return "RECEIVING";
		}
		
		/*
		 * nextState() moves RECEIVING to WAITING
		 * 
		 * input: none
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public FloorStatus nextState() {
			return WAITING;
		}
	},
	/*
	 * WAITING represents the state a floor takes when waiting on a request to be completed.
	 */
	WAITING {
		public String toString() {
			return "WAITING";
		}
		
		/*
		 * nextState() moves WAITING to SENDING
		 * 
		 * input: none
		 * output: ElevatorStatus
		 * 
		 */
		@Override
		public FloorStatus nextState() {
			return SENDING;
		}
	},
	/*
	 * ERROR represents the state a floor takes when an exception occurs.
	 */
	ERROR {
		public String toString() {
			return "ERROR";
		}
	};

	public FloorStatus nextState() {
		// TODO Auto-generated method stub
		return null;
	}
}
