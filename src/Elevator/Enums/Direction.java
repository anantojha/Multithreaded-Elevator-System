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
    
    /*
     * A constructor for the Direction 
     * 
     * input: value
     * output: none
     * 
     */
    Direction(int value) {
        this.directionId = value;
    }

    /*
     * getDirectionId() gets the direction id for the Direction 
     * 
     * input: none
     * output: int
     * 
     */
    public int getDirectionId() {
        return directionId;
    }

    /*
     * getDirectionFromId() gets the direction from the id
     * 
     * input: int
     * output: Direction
     * 
     */
    public static Direction getDirectionFromId(int id){
        if(id == 1){
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }
    }
}
