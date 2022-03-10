package Elevator;

import Elevator.ElevatorSubsystem.Elevator;
import Elevator.Enums.StateMachineStatus;
import Elevator.FloorSubsystem.Floor;
import Elevator.SchedulerSubsystem.Scheduler;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/*
 * The Main class represents the main program that all threads are initialized from. 
 * 
 */

public class    Main {

	/*
	 * main(String[] args) initializes the threads and State Machine, creates the requests, and begins the program.
	 * 
	 * Input: none
	 * Output: none
	 * 
	 */
    public static void main(String[] args) throws IOException, InterruptedException {
    	//Initialize State Machine
    	StateMachineState state = new StateMachineState(StateMachineStatus.INITIALIZE);
    	System.out.println("Program initializing.");
    	System.out.println("State Machine current state = " + state.getState());
    	
    	//Initialize Requests
    	System.out.println("\n\nInitializing sample floor requests.");
        createFloorCSV(1 , "FloorCSV", 5);
        Thread.sleep(100);

        //Transition State Machine to INITIALIZE state
    	state.setState(StateMachineStatus.INITIALIZE);

        Scheduler scheduler = new Scheduler();
        
        //Initialize Floors
    	System.out.println("\n\nInitializing floors.");

        //Create and start threads
        Thread floorOne = new Thread(new Floor(scheduler, 1), "Floor 1");
    	System.out.println("Initializing Elevator.");
        Thread elevatorOne = new Thread(new Elevator(scheduler, 1), "Elevator 1");
        Thread elevatorTwo = new Thread(new Elevator(scheduler, 2), "Elevator 2");
    	state.setState(StateMachineStatus.RUNNING);
    	System.out.println("\n\nStarting program.");
    	System.out.println("State Machine current state = " + state.getState());
        floorOne.start();
        elevatorOne.start();
        elevatorTwo.start();
        
        elevatorOne.join();
        elevatorTwo.join();
        
        //Upon completion, transition to the ENDING state and end the program
    	state.setState(StateMachineStatus.ENDING);
    	System.out.println("\n\nEnding program.");
    	System.out.println("State Machine current state = " + state.getState());
    }
    
	/*
	 * randomTimeDiff() creates a randomized difference in time and returns it.
	 * 
	 * Input: none
	 * Output: long
	 * 
	 */
    public static long randomTimeDiff(){
        return ThreadLocalRandom.current().nextLong(0,15);
    }


	/*
	 * createFloorCSV(int floors) creates a csv file for some number of floors specified with the following structure:
	 * [Time, floor, floor button, car button]
	 * 
	 * Input: int
	 * Output: none
	 * s
	 */
    public static void createFloorCSV(int floors, String folder, int numRequests) throws IOException {
        //Create CSV file for each floor
        for(int i = 1; i <= floors; i++){
            FileWriter csv = new FileWriter("CSV/" + folder + "/floor_" + i + ".csv");
            Random random = new Random();
            for(int j = 0; j < numRequests; j++){
                csv.append(LocalDateTime.now().toLocalTime().plusSeconds(randomTimeDiff()).toString());
                csv.append(",");
                csv.append(String.valueOf(i));
                csv.append(",");
                csv.append("UP");
                csv.append(",");
                csv.append((random.nextInt((5 - 2) + 1) + 2) + "");
                csv.append("\n");
            }
            csv.flush();
            csv.close();
        }
    }
}
