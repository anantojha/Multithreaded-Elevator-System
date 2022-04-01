package ElevatorTest;

import Elevator.Global.PacketHelper;
import Elevator.ElevatorSubsystem.Elevator;
import Elevator.FloorSubsystem.Floor;
import Elevator.SchedulerSubsystem.Scheduler;
import Elevator.FloorSubsystem.Request;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

public class FloorUDPTest {
	Thread floorOne, scheduler;
	DatagramSocket schedulerSocket;
	DatagramPacket receiveFloorPacket, sendReplyPacket;
	byte acknowledgementSignal[] = {1};
	byte[] taskRequest = {95, 1, 95};
	byte[] taskReceived;
	
	@Test
    public void sendRequests() throws IOException {
		Scheduler a = new Scheduler();
		Floor b = new Floor(1);
		scheduler = new Thread(a, "Scheduler");
    	floorOne = new Thread(b, "Floor 1");
		scheduler.start();
    	floorOne.start();
    	try {
    		floorOne.join();
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    		System.exit(1);
    	}
    	ArrayList<Request> sentRequests = b.getRequestList();
    	Queue<byte[]> receivedRequests = a.getQueue();
    	Assert.assertFalse(receivedRequests.isEmpty());
    	for (Request r : sentRequests) {
    		taskReceived = receivedRequests.poll();
    		Request received = PacketHelper.convertBytesToRequest(taskReceived);
    		Assert.assertEquals(r.toString(), received.toString());
    	}
	}
}
