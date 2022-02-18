package Elevator.Enums;
/*
 * Direction is a collection of constants representing the cardinal direction 
 * an elevator can take.
 * 
 */
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
