package Elevator.Global;

/*
 * SystemConfiguration is a wrapper class for all development variables.
 */
public final class SystemConfiguration {
    public static final Integer MAX_FLOOR = 22;                     // number of floors
    public static final Integer MAX_REQUESTS = 20;                  // total number of requests
    public static final Integer ELEVATORS = 4;                      // number of elevators
    public static final Integer FLOOR_REQUEST_TIME = 5;             // keep between 5 and 10
    public static final Integer SCHEDULER_ELEVATOR_PORT = 2506;     // scheduler socket port for elevator communication
    public static final Integer SCHEDULER_FLOOR_PORT = 2505;        // scheduler socket port for floor communication
    public static final Integer ELEVATOR_PORT = 2950;               // elevator socket port
    public static final Integer FLOOR_PORT = 3080;                  // floor socket port
}
