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
		@Override
		public String toString() {
			return "INITIALIZE";
		}
	},
	/*
	 * PROCESSING represents the state a floor takes when processing the CSV file of requests.
	 */
	PROCESSING {
		@Override
		public String toString() {
			return "PROCESSING";
		}
	},
	/*
	 * SENDING represents the state a floor takes when sending a request to the Scheduler.
	 */
	SENDING {
		public String toString() {
			return "SENDING";
		}
	},
	/*
	 * WAITING represents the state a floor takes when waiting on a request to be completed.
	 */
	WAITING {
		public String toString() {
			return "WAITING";
		}
	}
	 
}
