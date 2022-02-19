package Elevator.Enums;

public enum FloorStatus {
	INITIALIZE {
		@Override
		public String toString() {
			return "INITIALIZE";
		}
	},
	PROCESSING {
		@Override
		public String toString() {
			return "PROCESSING";
		}
	},
	SENDING {
		public String toString() {
			return "SENDING";
		}
	},
	WAITING {
		public String toString() {
			return "WAITING";
		}
	}
	 
}
