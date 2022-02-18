package Elevator;

import Elevator.ElevatorSubsystem.Elevator;
import Elevator.FloorSubsystem.Floor;
import Elevator.SchedulerSubsystem.Scheduler;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

/*
 * The Main class represents the main program that all threads are initialized from.
 * 
 */

public class    Main {

	/*
	 * main(String[] args) initializes the threads, creates the requests, and begins the program.
	 * 
	 * Input: none
	 * Output: none
	 * 
	 */
    public static void main(String[] args) throws IOException, InterruptedException {
        createFloorCSV(1 , "FloorCSV", 3);
        Thread.sleep(100);

        Scheduler scheduler = new Scheduler();
        
        //Create and start threads
        Thread floorOne = new Thread(new Floor(scheduler, 1), "Floor 1");
        Thread elevatorOne = new Thread(new Elevator(scheduler, 1), "Elevator 1");
        floorOne.start();
        elevatorOne.start();
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
                csv.append(LocalDateTime.now().toLocalTime().plusSeconds((j+1)*5).toString());
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
