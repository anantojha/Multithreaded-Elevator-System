package Elevator.Enums;

/*
 * SchedulerStatus is a collection of constants representing the states a scheduler can take.
 * 
 */
public enum SchedulerStatus {
	/*
	 * INITIALIZE represents the state a scheduler takes when currently initializing its variables.
	 */
	INITIALIZE {
		@Override
		public String toString() {
			return "INITIALIZE";
		}
	},
	/*
	 * CONNECTELEVATOR represents the state a scheduler takes when connecting to an elevator.
	 */
	CONNECTELEVATOR {
		@Override
		public String toString() {
			return "CONNECTED ELEVATOR";
		}
	},
	/*
	 * CONNECTFLOOR represents the state a scheduler takes when connecting to a floor.
	 */
	CONNECTFLOOR {
		@Override
		public String toString() {
			return "CONNECTED FLOOR";
		}
	},
	/*
	 * ADDINGREQUEST represents the state a scheduler takes when taking in a request.
	 */
	ADDINGREQUEST {
		@Override
		public String toString() {
			return "ADDING REQUEST";
		}
	},
	/*
	 * SENDREQUEST represents the state a scheduler takes when a request response has been sent.
	 */
	SENDREQUEST {
		public String toString() {
			return "SEND REQUEST";
		}
	},
	/*
	 * COMPLETEREQUEST represents the state a scheduler takes when a request has been completed.
	 */
	COMPLETEREQUEST {
		public String toString() {
			return "COMPLETE REQUEST";
		}
	}
}
