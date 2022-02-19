package Elevator.Enums;

public enum ElevatorStatus {
    RUNNING {
        @Override
        public String toString() {
            return "RUNNING";
        }
    },
    ARRIVED {
        @Override
        public String toString() {
            return "ARRIVED";
        }
    },
    IDLE {
        @Override
        public String toString() {
            return "IDLE";
        }
    },
    OPEN_DOOR {
        @Override
        public String toString() {
            return "OPEN DOOR";
        }
    },
    CLOSE_DOOR {
        @Override
        public String toString() {
            return "CLOSE DOOR";
        }
    }
}
