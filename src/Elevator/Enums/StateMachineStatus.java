package Elevator.Enums;
/*
 * StateMachineStatus is a collection of constants representing the states a scheduler can take.
 * 
 */
public enum StateMachineStatus {
	/*
	 * INITIALIZE represents the state a State Machine takes when currently initializing its variables.
	 */
	INITIALIZE {
		@Override
		public String toString() {
			return "INITIALIZE";
		}
	},
	/*
	 * RUNNING represents the state a State Machine takes when currently active.
	 */
	RUNNING {
		@Override
		public String toString() {
			return "RUNNING";
		}
	},
	/*
	 * ENDING represents the state a State Machine takes when it finishes.
	 */
	ENDING {
		@Override
		public String toString() {
			return "ENDING";
		}
	}

}
