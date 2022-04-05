package ElevatorTest;

import Elevator.Global.PacketHelper;
import Elevator.FloorSubsystem.Floor;
import Elevator.SchedulerSubsystem.Scheduler;
import Elevator.FloorSubsystem.Request;

import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

public class FloorUDPTest {
	Thread floorOne, scheduler;
	Scheduler a;
	Floor b;
	byte[] taskReceived;
	
	@Before
	public void setup() throws IOException {
		//Create scheduler and floor threads then start them
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
    		Assert.assertEquals(r.toString(), received.toString());
    	}
	}
}
