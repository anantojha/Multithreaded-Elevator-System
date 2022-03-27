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
	},
	/*
	 * PROCESSING represents the state a floor takes when processing the CSV file of requests.
	 */
	PROCESSING {
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
	 * RECEIVING represents the state a floor takes when receiving a response from the Scheduler.
	 */
	RECEIVING {
		public String toString() {
			return "RECEIVING";
		}
	},
	/*
	 * WAITING represents the state a floor takes when waiting on a request to be completed.
	 */
	WAITING {
		public String toString() {
			return "WAITING";
		}
	},
	/*
	 * ERROR represents the state a floor takes when an exception occurs.
	 */
	ERROR {
		public String toString() {
			return "ERROR";
		}
	}
}
