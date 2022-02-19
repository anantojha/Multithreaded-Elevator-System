package Elevator.Enums;

public enum SchedulerStatus {

	INITIALIZE {
		@Override
		public String toString() {
			return "INITIALIZE";
		}
	},
	ADDINGREQUEST {
		@Override
		public String toString() {
			return "ADDINGREQUEST";
		}
	},
	PRINTREQUEST {
		public String toString() {
			return "PRINTREQUEST";
		}
	},
	COMPLETEREQUEST {
		public String toString() {
			return "COMPLETEREQUEST";
		}
	}
}
