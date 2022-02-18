package ElevatorTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import Elevator.FloorSubsystem.Floor;
import Elevator.SchedulerSubsystem.Scheduler;
import Elevator.Request;
import Elevator.Enums.Direction;
import java.util.ArrayList;

public class FloorTest {
	Thread floorThread;
	Floor floor;
	Scheduler scheduler;
	ArrayList<Request> Requests; 
	Request serviceRequest;
	
	@Before
    public void setup(){
		scheduler = new Scheduler();
		floor = new Floor(scheduler, 1);
		floorThread = new Thread(floor, "Floor 1");
		Requests = new ArrayList<>();
	}
	
	@Test
    public void receiveAndSendRequests() throws InterruptedException, IOException{
		//generate csv file for floor1
		Elevator.Main.createFloorCSV(1, "FloorCSV", 5);
		
		//Read csv file and add incoming requests to Requests arraylist
		try (BufferedReader br = new BufferedReader(new FileReader("CSV/FloorCSV/floor_1.csv"))) {
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
			Assert.assertEquals(r.toString(), serviceRequest.toString());
			scheduler.serviceRequest(serviceRequest, 1);
		}
	}
}
