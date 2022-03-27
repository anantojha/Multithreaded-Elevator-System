package Elevator.Enums;
/*
 * Direction is a collection of constants representing the cardinal direction 
 * an elevator can take.
 * 
 */
public enum Direction {
    UP(1) {
        @Override
        public String toString() {
            return "UP";
        }
    },
    DOWN(2) {
        @Override
        public String toString() {
            return "DOWN";
        }
    };

    private final int directionId;

    Direction(int value) {
        this.directionId = value;
    }

    public int getDirectionId() {
        return directionId;
    }

    public static Direction getDirectionFromId(int id){
        if(id == 1){
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }
    }
}
