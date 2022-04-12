package ElevatorTest;//package ElevatorTest;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import Elevator.ElevatorSubsystem.Elevator;
import Elevator.ElevatorSubsystem.ElevatorController;
import Elevator.FloorSubsystem.Floor;
import Elevator.FloorSubsystem.Request;
import Elevator.SchedulerSubsystem.Scheduler;
import GUI.ControlPanelGUI;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

// make GUI window full screen when running test

/*
 * SystemTest tests the entire system with 4 elevators and 22 floors. 
 * The test creates an input file with 15 requests and checks the system properly services all tasks.
 */
public class SystemTest {

    Thread elevatorOne, elevatorTwo, elevatorThree, elevatorFour, elevatorControllerOne, elevatorControllerTwo, elevatorControllerThree, elevatorControllerFour;
    Thread floorOne, floorTwo, floorThree, floorFour, floorFive, floorSix, floorSeven, floorEight, floorNine, floorTen, floorEleven, floorTwelve, floorThirteen;
    Thread floorFourteen, floorFifteen, floorSixteen, floorSeventeen, floorEighteen, floorNineteen, floorTwenty, floorTwentyOne, floorTwentyTwo;
    Thread schedulerThread;
    Elevator a, b, c, d;
    ControlPanelGUI gui;
    Scheduler scheduler;

	@Before
	/*
	 * setup() performs necessary setup to perform test case. Creates a scheduler instance, 4 elevator instances, 22 floor instances, and the GUI.
	 * It also creates the input file for the system with 15 generated requests to service.
	 * 
	 * Input: None 
	 * Output: None
	 */
    public void setup() throws IOException, InterruptedException {
        //create scheduler instance & scheduler thread
        scheduler = new Scheduler();
        schedulerThread = new Thread(scheduler, "Scheduler Thread");

        //create Queue of Requests for elevator threads 
        Queue<Request> jobs = new LinkedBlockingQueue<>();
        
        //Create gui for displaying 4 elevators
        gui = new ControlPanelGUI(4);
        
        //create 4 elevator threads
        a = new Elevator(1, jobs, gui);
        b = new Elevator(2, jobs, gui);
        c = new Elevator(3, jobs, gui);
        d = new Elevator(4, jobs, gui);
        elevatorOne = new Thread(a, "Elevator 1");
        elevatorControllerOne = new Thread(new ElevatorController(1, jobs), "Elevator Controller 1");
        elevatorTwo = new Thread(b, "Elevator 2");
        elevatorControllerTwo = new Thread(new ElevatorController(2, jobs), "Elevator Controller 2");
        elevatorThree = new Thread(c, "Elevator 3");
        elevatorControllerThree = new Thread(new ElevatorController(3, jobs), "Elevator Controller 3");
        elevatorFour = new Thread(d, "Elevator 4");
        elevatorControllerFour = new Thread(new ElevatorController(4, jobs), "Elevator Controller 4");

        //create 22 floor threads
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
        floorEleven = new Thread(new Floor(11), "Floor 11");
        floorTwelve = new Thread(new Floor(12),"Floor 12");
        floorThirteen = new Thread(new Floor(13),"Floor 13");
        floorFourteen = new Thread(new Floor(14),"Floor 14");
        floorFifteen = new Thread(new Floor(15),"Floor 15");
        floorSixteen = new Thread(new Floor(16), "Floor 16");
        floorSeventeen = new Thread(new Floor(17),"Floor 17");
        floorEighteen = new Thread(new Floor(18),"Floor 18");
        floorNineteen = new Thread(new Floor(19),"Floor 19");
        floorTwenty = new Thread(new Floor(20),"Floor 20");
        floorTwentyOne = new Thread(new Floor(21),"Floor 21");
        floorTwentyTwo = new Thread(new Floor(22),"Floor 22");
        
        
        //Create CSV file for floor requests
        Floor.createFloorCSV(22, "FloorCSV", 15);
	}

	@Test
	/*
	 * receiveAndSendRequests() starts all the threads and runs the system until all generated requests have been sent by a floor thread.
	 * The floor should complete, the scheduler should send all requests to an elevator, and the elevators should have serviced as many requests
	 * before the floor threads complete sending requests.
	 * 
	 * Input: None
	 * Output: None
	 */
    public void receiveAndSendRequests() throws InterruptedException {
		//start all threads
        schedulerThread.start();
        elevatorControllerOne.start();
        elevatorOne.start();
        elevatorControllerTwo.start();
        elevatorTwo.start();
        elevatorControllerThree.start();
        elevatorThree.start();
        elevatorControllerFour.start();
        elevatorFour.start();
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
        floorEleven.start();
        floorTwelve.start();
        floorThirteen.start();
        floorFourteen.start();
        floorFifteen.start();
        floorSixteen.start();
        floorSeventeen.start();
        floorEighteen.start();
        floorNineteen.start();
        floorTwenty.start();
        floorTwentyOne.start();
        floorTwentyTwo.start();
        
        //boolean to check for when all floor threads complete
        Boolean floorThreadsFinished = true;
        
        //infinite loop until floor threads complete
        while(floorThreadsFinished) {
        	//communication with elevator threads
        	scheduler.elevatorHandle();
        	//check if all floor threads completed
        	floorThreadsFinished = (floorOne.isAlive() || floorTwo.isAlive() || floorThree.isAlive() || floorFour.isAlive() || floorFive.isAlive()
            		|| floorSix.isAlive() || floorSeven.isAlive() || floorEight.isAlive() || floorNine.isAlive() || floorTen.isAlive() || floorEleven.isAlive()
            		|| floorTwelve.isAlive() || floorThirteen.isAlive() || floorFourteen.isAlive() || floorFifteen.isAlive() || floorSixteen.isAlive()
            		|| floorSeventeen.isAlive() || floorEighteen.isAlive() || floorNineteen.isAlive() || floorTwenty.isAlive() || floorTwentyOne.isAlive()
            		|| floorTwentyTwo.isAlive());
        }
        
        //assert scheduler sent all requests after floor threads finish sending
        Assert.assertTrue(scheduler.getQueue().isEmpty()); 
        //assert elevator and scheduler threads are still active after floor threads finish sending
        Assert.assertTrue(elevatorOne.isAlive());
        Assert.assertTrue(elevatorControllerOne.isAlive());
        Assert.assertTrue(elevatorTwo.isAlive());
        Assert.assertTrue(elevatorControllerTwo.isAlive());
        Assert.assertTrue(elevatorThree.isAlive());
        Assert.assertTrue(elevatorControllerThree.isAlive());
        Assert.assertTrue(elevatorFour.isAlive());
        Assert.assertTrue(elevatorControllerFour.isAlive());
        Assert.assertTrue(schedulerThread.isAlive());
	}

}
