package ElevatorTest;//package ElevatorTest;
//
//import java.io.File;
//import java.io.IOException;
//
//import Elevator.ElevatorSubsystem.Elevator;
//import Elevator.FloorSubsystem.Floor;
//import Elevator.SchedulerSubsystem.Scheduler;
//import org.junit.Before;
//import org.junit.After;
//import org.junit.Test;
//import org.junit.Assert;
//
//public class SystemTest {
//
//    Thread elevatorOne, elevatorTwo;
//    Thread floorOne, floorTwo, floorThree, floorFour, floorFive, floorSix, floorSeven, floorEight, floorNine, floorTen;
//    Thread schedulerThread;
//    Scheduler scheduler;
//	File TestFolder;
//
//	@Before
//    public void setup() throws IOException {
//        //create scheduler instance
//        scheduler = new Scheduler(new Scheduler(), "Elevator 1");
//
//        //create test csv folder if one doesn't exist
//        TestFolder = new File("CSV/TestFloorCSV");
//        if(!TestFolder.exists()){
//            TestFolder.mkdir();
//        } else {
//            // if folder exists, delete all files from folder
//            for(File f: TestFolder.listFiles())
//                if (!f.isDirectory())
//                    f.delete();
//        }
//
//        //create and start scheduler thread using scheduler instance
//        schedulerThread = new Thread(() -> {
//            try {
//                scheduler.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        schedulerThread.start();
//
//        //create and start 2 elevator threads
//        elevatorOne = new Thread(new Elevator("1"), "Elevator 1");
//        elevatorTwo = new Thread(new Elevator("2"), "Elevator 2");
//        elevatorOne.start();
//        elevatorTwo.start();
//
//        //create 10 floor threads
//        floorOne = new Thread(new Floor(1), "Floor 1");
//        floorTwo = new Thread(new Floor(2),"Floor 2");
//        floorThree = new Thread(new Floor(3),"Floor 3");
//        floorFour = new Thread(new Floor(4),"Floor 4");
//        floorFive = new Thread(new Floor(5),"Floor 5");
//
//        floorSix = new Thread(new Floor(6), "Floor 6");
//        floorSeven = new Thread(new Floor(7),"Floor 7");
//        floorEight = new Thread(new Floor(8),"Floor 8");
//        floorNine = new Thread(new Floor(9),"Floor 9");
//        floorTen = new Thread(new Floor(10),"Floor 10");
//	}
//
//	@Test
//    public void receiveAndSendRequests() throws InterruptedException {
//		//start and join 10 floor threads
//        floorOne.start();
//        floorTwo.start();
//        floorThree.start();
//        floorFour.start();
//        floorFive.start();
//        floorSix.start();
//        floorSeven.start();
//        floorEight.start();
//        floorNine.start();
//        floorTen.start();
//
//        floorOne.join();
//        floorTwo.join();
//        floorThree.join();
//        floorFour.join();
//        floorFive.join();
//        floorSix.join();
//        floorSeven.join();
//        floorEight.join();
//        floorNine.join();
//        floorTen.join();
//
//        //assert queue of requests is empty after system finishes executing
//        Assert.assertTrue(scheduler.getQueue().isEmpty());
//	}
//		@After
//	public void tearDown() {
//		//delete files made for SystemTest.java
//		for (File f: TestFolder.listFiles()) {
//			if (!f.isDirectory()) {
//				f.delete();
//			}
//		}
//	}
//}
