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
	 * ADDINGREQUEST represents the state a scheduler takes when taking in a request.
	 */
	ADDINGREQUEST {
		@Override
		public String toString() {
			return "ADDINGREQUEST";
		}
	},
	/*
	 * PRINTREQUEST represents the state a scheduler takes when printing out the request.
	 */
	PRINTREQUEST {
		public String toString() {
			return "PRINTREQUEST";
		}
	},
	/*
	 * COMPLETEREQUEST represents the state a scheduler takes when a request has been completed.
	 */
	COMPLETEREQUEST {
		public String toString() {
			return "COMPLETEREQUEST";
		}
	}
}
