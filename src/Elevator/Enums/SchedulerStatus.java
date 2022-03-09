package Elevator.Enums;

public enum SchedulerStatus {

	INITIALIZE {
		@Override
		public String toString() {
			return "INITIALIZE";
		}
	},
	CONNECTELEVATOR {
		@Override
		public String toString() {
			return "CONNECTED ELEVATOR";
		}
	},
	CONNECTFLOOR {
		@Override
		public String toString() {
			return "CONNECTED FLOOR";
		}
	},
	ADDINGREQUEST {
		@Override
		public String toString() {
			return "ADDING REQUEST";
		}
	},
	SENDREQUEST {
		public String toString() {
			return "SEND REQUEST";
		}
	},
	COMPLETEREQUEST {
		public String toString() {
			return "COMPLETE REQUEST";
		}
	}
}
