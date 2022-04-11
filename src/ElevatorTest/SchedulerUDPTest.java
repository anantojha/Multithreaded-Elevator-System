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

/*
 * SchedulerUDPTest tests the Scheduler's UDP connections with the elevators and floors.
 * The test creates a DatagramSocket to imitate the floor and uses it to send a set task to the scheduler.
 * The scheduler should receive the task from the floor, add task to queue, then send task to elevator to service once it receives request for task from elevator.
 */
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
	/*
	 * setup() performs necessary setup to perform test case. Creates a socket imitating floor, a packet to be sent to the scheduler with a set task,
	 * the scheduler instance, the GUI, the elevator to test, and the elevator's controller.
	 * 
	 * Input: None 
	 * Output: None
	 */
	public void setup() throws IOException, InterruptedException {
		//Create socket for imitating floor and a DatagramPacket with a set request (Source: 5, Destination: 1)
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
	/*
	 * sendAndReceiveRequests() starts the scheduler and elevator threads and checks all UDP messages send and received are correctly relayed.
	 * The floor sends the task to the scheduler, then the scheduler should receive it, add it to it's queue then send it to elevator once scheduler
	 * receives request for task from elevator.
	 * 
	 * Input: None
	 * Output: None
	 */
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
        	//Check if scheduler properly sent task to elevator
        	Assert.assertTrue(receivedRequests.isEmpty());
        }
        //Let elevator thread run until it completes the task
        Thread.sleep(13500);
        
        //After elevator completes request check there are no requests remaining
        receivedJobs = b.getJobs();
        Assert.assertTrue(receivedJobs.size() == 0);
	}

}
