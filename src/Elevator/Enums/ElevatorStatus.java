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
    CLOSING_DOORS {
        @Override
        public String toString() {
            return "CLOSING DOORS";
        }
    },
    OPENING_DOORS {
        @Override
        public String toString() {
            return "OPENING DOORS";
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
