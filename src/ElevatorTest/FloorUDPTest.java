package ElevatorTest;

import Elevator.Global.PacketHelper;
import Elevator.FloorSubsystem.Floor;
import Elevator.SchedulerSubsystem.Scheduler;
import Elevator.FloorSubsystem.Request;

import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

public class FloorUDPTest {
	Thread floorOne, scheduler;
	Scheduler a;
	Floor b;
	byte[] taskReceived;
	File Folder;
	FileWriter csv; 
	Random random = new Random();
	
	@Before
	public void setup() throws IOException {
		Folder = new File("CSV/FloorCSV");
		// create csv folder if one doesn't exist
        if(!Folder.exists()){
            Folder.mkdir();
        } else {
            // if folder exists, delete all files from folder
            for(File f: Folder.listFiles())
                if (!f.isDirectory())
                    f.delete();
        }
		csv = new FileWriter("CSV/FloorCSV/floor.csv");
        //Manually input a request from floor 1 as source into csv input file
		LocalTime timeCount = LocalDateTime.now().toLocalTime().plusSeconds(random.nextInt(7) + 7);
		csv.append(timeCount.toString());
		csv.append(",");
        csv.append("1");
        csv.append(",");
        csv.append("UP");
        csv.append(",");
        csv.append(String.valueOf(random.nextInt(10)+1) + "");
        csv.append(",");
        csv.append("f");
        csv.append("\n");
		csv.flush();
		csv.close();
		//Create scheduler and floor threads and start them
		a = new Scheduler();
		b = new Floor(1);
		scheduler = new Thread(a, "Scheduler");
		floorOne = new Thread(b, "Floor 1");
		scheduler.start();
		floorOne.start();
	}
	
	@Test
    public void sendRequests() throws IOException, InterruptedException {
    	//Join floor thread
    	floorOne.join();
    	
    	//Get requests generated from floor
    	ArrayList<Request> sentRequests = b.getRequestList();
    	//Get queue of requests received by scheduler
    	Queue<byte[]> receivedRequests = a.getQueue();
    	
    	//Check received requests is same as generated requests
    	Assert.assertFalse(receivedRequests.isEmpty());
    	for (Request r : sentRequests) {
    		taskReceived = receivedRequests.poll();
    		Request received = PacketHelper.convertBytesToRequest(taskReceived);
    		System.out.println("Sent request: " + r.toString());
    		System.out.println("Received request: " + received.toString());
    		Assert.assertEquals(r.toString(), received.toString());
    	}
	}
}
