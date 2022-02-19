package ElevatorTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Assert;

import Elevator.FloorSubsystem.Floor;
import Elevator.SchedulerSubsystem.Scheduler;
import Elevator.FloorSubsystem.Request;
import Elevator.Enums.Direction;
import Elevator.Enums.FloorStatus;
import java.util.ArrayList;

public class FloorTest {
	Thread floorThread;
	Floor floor;
	Scheduler scheduler;
	ArrayList<Request> Requests; 
	Request serviceRequest;
	File TestFolder;
	
	@Before
    public void setup(){
	    	//create scheduler and floor class, thread for floor, and arraylist to hold requests
		scheduler = new Scheduler();
		floor = new Floor(scheduler, 1);
	    	floor.setTestEnabled(true);
		floorThread = new Thread(floor, "Floor 1");
		Requests = new ArrayList<>();
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
        	Assert.assertEquals(FloorStatus.INITIALIZE, floor.getCurrentState());
	}
	
	@Test
    public void receiveAndSendRequests() throws InterruptedException, IOException{
		//generate csv file for floor1
		Elevator.Main.createFloorCSV(1, "TestFloorCSV", 5);
		
		//Read csv file and add incoming requests to Requests arraylist
		try (BufferedReader br = new BufferedReader(new FileReader("CSV/TestFloorCSV/floor_1.csv"))) {
            String line;
            //For each line in the csv, find the appropriate Direction, and add it to the request
            while ((line = br.readLine()) != null) {
                String[] requestContents = line.split(",");
                for(Direction d: Direction.values()){
                    if(d.toString().equals(requestContents[2])){
                        LocalDate date = LocalDateTime.now().toLocalDate();
                        Requests.add(new Request(LocalDateTime.parse(date.toString() + "T" + requestContents[0]), Integer.parseInt(requestContents[1]),
                                d, Integer.parseInt(requestContents[3])));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		//start floor thread and check floor thread properly sends request to scheduler 
		floorThread.start();
		for (Request r: Requests) {
			serviceRequest = scheduler.getRequest();
			Assert.assertEquals(FloorStatus.WAITING, floor.getCurrentState());
			Assert.assertEquals(r.toString(), serviceRequest.toString());
			scheduler.serviceRequest(serviceRequest, 1);
		}
	}
		@After 
	public void tearDown() {
		//delete files made for FloorTest.java
		for (File f: TestFolder.listFiles()) {
			if (!f.isDirectory()) {
				f.delete();
			}
		}
	}
}
