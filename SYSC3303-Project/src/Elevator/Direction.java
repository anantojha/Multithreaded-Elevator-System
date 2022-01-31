package Elevator;

public enum Direction {
    UP {
        @Override
        public String toString() {
            return "UP";
        }
    },
    DOWN {
        @Override
        public String toString() {
            return "DOWN";
        }
    }
}
