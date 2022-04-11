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
import GUI.ControlPanelGUI;

public class SchedulerUDPTest {
	Thread elevator, elevatorController, scheduler;
	Queue<Request> jobs = new LinkedBlockingQueue<>();
	Queue<Request> receivedJobs;
	byte[] task = {95, 1, 95, 50, 48, 50, 50, 45, 48, 52, 45, 48, 57, 84, 48, 49, 58, 48, 48, 58, 52, 57, 46, 53, 48, 51, 53, 54, 52, 95, 2, 95, 5, 95, 1, 95, 0, 95};
	DatagramPacket job;
	DatagramSocket floor;
	Scheduler a;
	Elevator b;
	ControlPanelGUI gui;
	
	@Before
	public void setup() throws IOException, InterruptedException {
		//Create socket for imitating floor and a DatagramPacket with a set request
		floor = new DatagramSocket();
		InetAddress localHostVar = InetAddress.getLocalHost();;
		job = new DatagramPacket(task, task.length, localHostVar, 2505);	
		
		//Create scheduler, GUI, elevator, and elevator controller threads
		a = new Scheduler();
		gui = new ControlPanelGUI(1);
		b = new Elevator(1, jobs, gui);
		scheduler = new Thread(a, "Scheduler");
		elevatorController = new Thread(new ElevatorController(1, jobs), "ElevatorController 1");
        elevator = new Thread(b, "Elevator 1");
	}
	
	@Test
    public void sendAndReceiveRequests() throws IOException, InterruptedException {
		//Start threads
        scheduler.start();
        elevatorController.start();
        elevator.start();
        
        //Send request as if floor was sending it
    	floor.send(job);
    	
    	//Check if scheduler received request
        Queue<byte[]> receivedRequests = a.getQueue();
        Thread.sleep(100);
        Assert.assertTrue(receivedRequests.size() == 1);
        
        //Begin elevator communication
        while (!receivedRequests.isEmpty()) {
        	a.elevatorHandle();
        	//Check if elevator properly received request
        	Assert.assertTrue(receivedRequests.size() == 0);
        }
        //Let elevator thread run
        Thread.sleep(13500);
        //After elevator completes request check there are no requests remaining
        receivedJobs = b.getJobs();
        Assert.assertTrue(receivedJobs.size() == 0);
	}

}
