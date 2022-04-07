package ElevatorTest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Elevator.ElevatorSubsystem.Elevator;
import Elevator.ElevatorSubsystem.ElevatorController;
import Elevator.FloorSubsystem.Request;

public class ElevatorUDPTest {
	Thread elevator, elevatorController;
	Queue<Request> jobs = new LinkedBlockingQueue<>();
	Queue<Request> receivedJobs;
	byte[] task = {95, 1, 95, 50, 48, 50, 50, 45, 48, 52, 45, 48, 52, 84, 49, 54, 58, 51, 48, 58, 48, 54, 46, 51, 51, 51, 53, 51, 56, 55, 48, 48, 95, 1, 95, 1, 95, 7, 95};
	byte[] requestJob = {95, 1, 95};
	DatagramPacket job, requestPacket;
	DatagramSocket scheduler;
	Elevator a;
	
	@Before
	public void setup() throws IOException {
		//Create socket for imitating floor and a DatagramPacket with a set request
		scheduler = new DatagramSocket(2506);
		InetAddress localHostVar = InetAddress.getLocalHost();;
		job = new DatagramPacket(task, task.length, localHostVar, 2951);	
		
		//Create scheduler. elevator, and elevator controller threads
		a = new Elevator(jobs);
		elevatorController = new Thread(new ElevatorController("1", jobs), "ElevatorController 1");
        elevator = new Thread(a, "Elevator 1");
	}
	
	@Test
	public void receiveRequests() throws IOException, InterruptedException {
		//Start elevator threads
		elevatorController.start();
        elevator.start();
        
		//Receive elevator thread request for packet
		byte data[] = new byte[3];
		requestPacket = new DatagramPacket(data, data.length);
		scheduler.receive(requestPacket);
        Assert.assertEquals(Arrays.toString(requestJob), Arrays.toString(requestPacket.getData()));
        
        //Send task to elevator thread
        scheduler.send(job);
        //Let elevator thread run 
        Thread.sleep(11000);
        
        //Check elevator completes and 
        receivedJobs = a.getJobs();
        Assert.assertTrue(receivedJobs.size() == 0);
	}

}
