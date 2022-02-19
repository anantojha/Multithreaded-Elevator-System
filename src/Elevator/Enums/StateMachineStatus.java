package Elevator.Enums;

public enum StateMachineStatus {
	//Initialize threads
	INITIALIZE {
		@Override
        public String toString() {
            return "INITIALIZE";
        }
	},
	RUNNING {
		@Override
        public String toString() {
            return "RUNNING";
        }
	},
	ENDING {
		@Override
        public String toString() {
            return "ENDING";
        }
	}
	
}
