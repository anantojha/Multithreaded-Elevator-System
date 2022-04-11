package Elevator.Enums;

/*
 * SubSystemMapping is an enum of the different sub-systems.
 * 
 */
public enum SubSystemMapping {

    FLOOR(1), ELEVATOR(2), SCHEDULER(3);

    private final int subSystemId;

    SubSystemMapping(int value) {
        this.subSystemId = value;
    }

    public int subSystemId() {
        return subSystemId;
    }
}
