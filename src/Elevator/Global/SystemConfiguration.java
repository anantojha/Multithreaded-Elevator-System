package Elevator.Global;

/*
 * SystemConfiguration is a wrapper class for all development variables.
 */
public final class SystemConfiguration {
    public static final Integer MAX_FLOOR = 22;
    public static final Integer MAX_REQUESTS = 20;
    public static final Integer ELEVATORS = 3; // do not change
    public static final Integer SCHEDULER_ELEVATOR_PORT = 2506; // scheduler port for elevator communication
    public static final Integer SCHEDULER_FLOOR_PORT = 2505;    // scheduler port for floor communication
    public static final Integer ELEVATOR_PORT = 2950;
    public static final Integer FLOOR_PORT = 3080;
}
