package ElevatorTest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;

import Elevator.ElevatorSubsystem.*;
import Elevator.FloorSubsystem.Request;
import Elevator.SchedulerSubsystem.Scheduler;

public class ElevatorUDPTest {
	Thread elevator, elevatorController, scheduler;
	Queue<Request> jobs = new LinkedBlockingQueue<>();
	Queue<Request> receivedJobs;
	byte[] task = {95, 1, 95, 50, 48, 50, 50, 45, 48, 52, 45, 48, 52, 84, 49, 54, 58, 51, 48, 58, 48, 54, 46, 51, 51, 51, 53, 51, 56, 55, 48, 48, 95, 1, 95, 1, 95, 7, 95};
	DatagramPacket job;
	DatagramSocket floor;
	
	@Before
	public void setup() throws IOException {
		floor = new DatagramSocket();
		InetAddress localHostVar = InetAddress.getLocalHost();;
		job = new DatagramPacket(task, task.length, localHostVar, 2505);	
	}
	
	@Test
    public void receiveRequests() throws IOException, InterruptedException {
		Scheduler a = new Scheduler();
		Elevator b = new Elevator(jobs);
		scheduler = new Thread(a, "Scheduler");
		elevatorController = new Thread(new ElevatorController("1", jobs), "ElevatorController 1");
        elevator = new Thread(b, "Elevator 1");
        scheduler.start();
        elevatorController.start();
        elevator.start();
    	floor.send(job);
        Queue<byte[]> receivedRequests = a.getQueue();
        Thread.sleep(100);
        Assert.assertTrue(receivedRequests.size() == 1);
        while (!receivedRequests.isEmpty()) {
        	a.elevatorHandle();
        	Assert.assertTrue(receivedRequests.size() == 0);
        }
        Thread.sleep(11000);
        receivedJobs = b.getJobs();
        Assert.assertTrue(receivedJobs.size() == 0);
	}

}
