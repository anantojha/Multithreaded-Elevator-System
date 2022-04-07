package ElevatorTest;//package ElevatorTest;

import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import Elevator.ElevatorSubsystem.Elevator;
import Elevator.ElevatorSubsystem.ElevatorController;
import Elevator.FloorSubsystem.Floor;
import Elevator.FloorSubsystem.Request;
import Elevator.SchedulerSubsystem.Scheduler;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Assert;

public class SystemTest {

    Thread elevatorOne, elevatorTwo, elevatorControllerOne, elevatorControllerTwo;
    Thread floorOne, floorTwo, floorThree, floorFour, floorFive, floorSix, floorSeven, floorEight, floorNine, floorTen;
    Thread schedulerThread;
    Scheduler scheduler;
	File TestFolder;

	@Before
    public void setup() throws IOException {
        //create scheduler instance & scheduler thread
        scheduler = new Scheduler();
        schedulerThread = new Thread(scheduler, "Scheduler Thread");
        
        //create test csv folder if one doesn't exist
        TestFolder = new File("CSV/TestFloorCSV");
        if(!TestFolder.exists()){
            TestFolder.mkdir();
        } else {
            // if folder exists, delete all files from folder
            for(File f: TestFolder.listFiles())
                if (!f.isDirectory())
                    f.delete();
        }
        //create Queue of Requests for elevator threads 
        Queue<Request> jobs = new LinkedBlockingQueue<>();
        
        //create and start 2 elevator threads
        elevatorOne = new Thread(new Elevator(jobs), "Elevator 1");
        elevatorControllerOne = new Thread(new ElevatorController("1", jobs), "Elevator Controller 1");
        elevatorTwo = new Thread(new Elevator(jobs), "Elevator 2");
        elevatorControllerTwo = new Thread(new ElevatorController("2", jobs), "Elevator Controller 2");

        //create 10 floor threads
        floorOne = new Thread(new Floor(1), "Floor 1");
        floorTwo = new Thread(new Floor(2),"Floor 2");
        floorThree = new Thread(new Floor(3),"Floor 3");
        floorFour = new Thread(new Floor(4),"Floor 4");
        floorFive = new Thread(new Floor(5),"Floor 5");
        floorSix = new Thread(new Floor(6), "Floor 6");
        floorSeven = new Thread(new Floor(7),"Floor 7");
        floorEight = new Thread(new Floor(8),"Floor 8");
        floorNine = new Thread(new Floor(9),"Floor 9");
        floorTen = new Thread(new Floor(10),"Floor 10");
        
        //Create CSV file for floor requests
        Floor.createFloorCSV(10, "FloorCSV", 10);
	}

	@Test
    public void receiveAndSendRequests() throws InterruptedException {
		//start all threads
        schedulerThread.start();
		floorOne.start();
        floorTwo.start();
        floorThree.start();
        floorFour.start();
        floorFive.start();
        floorSix.start();
        floorSeven.start();
        floorEight.start();
        floorNine.start();
        floorTen.start();
        elevatorControllerOne.start();
        elevatorOne.start();
        elevatorControllerTwo.start();
        elevatorTwo.start();
        
        //boolean to check for when all floor threads complete
        Boolean floorThreadsFinished = true;
        
        //infinite loop until floor threads complete
        while(floorThreadsFinished) {
        	//communication with elevator threads
        	scheduler.elevatorHandle();
        	//check if all floor threads completed
        	floorThreadsFinished = (floorOne.isAlive() || floorTwo.isAlive() || floorThree.isAlive() || floorFour.isAlive() || floorFive.isAlive()
            		|| floorSix.isAlive() || floorSeven.isAlive() || floorEight.isAlive() || floorNine.isAlive() || floorTen.isAlive());
        }
        
        //assert queue of requests is empty after system finishes
        Assert.assertTrue(scheduler.getQueue().isEmpty()); 
        Assert.assertTrue(elevatorOne.isAlive());
        Assert.assertTrue(elevatorTwo.isAlive());
        Assert.assertTrue(schedulerThread.isAlive());
	}
	
	@After
	public void tearDown() {
		//delete files made for SystemTest.java
		for (File f: TestFolder.listFiles()) {
			if (!f.isDirectory()) {
				f.delete();
			}
		}
	}
}
