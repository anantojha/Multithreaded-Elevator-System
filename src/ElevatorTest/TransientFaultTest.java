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
import Elevator.Global.PacketHelper;
import GUI.MainGUI;

public class TransientFaultTest {
	Thread elevator, elevatorController;
	Queue<Request> jobs = new LinkedBlockingQueue<>();
	Queue<Request> receivedJobs;
	byte[] task = {95, 1, 95, 50, 48, 50, 50, 45, 48, 52, 45, 48, 57, 84, 48, 49, 58, 48, 48, 58, 52, 57, 46, 53, 48, 51, 53, 54, 52, 95, 2, 95, 5, 95, 4, 95, 0, 95};
	byte[] fault = {95, 1, 95, 50, 48, 50, 50, 45, 48, 52, 45, 48, 57, 84, 48, 49, 58, 48, 48, 58, 52, 57, 46, 53, 48, 51, 53, 54, 52, 95, 2, 95, 9, 95, 3, 95, 2, 95};
	byte[] requestJob = {95, 1, 95};
	DatagramPacket job, faultJob, requestPacket;
	DatagramSocket scheduler;
	Elevator a;
	MainGUI gui;
	
	@Before
	public void setup() throws IOException {
		//Create socket for imitating scheduler and a DatagramPacket with a set request and a request with a hard fault
		scheduler = new DatagramSocket(2506);
		InetAddress localHostVar = InetAddress.getLocalHost();
		job = new DatagramPacket(task, task.length, localHostVar, 2951);	
		faultJob = new DatagramPacket(fault, fault.length, localHostVar, 2951);
		//Create scheduler, GUI, elevator, and elevator controller threads then add elevator to GUI
		a = new Elevator(1, jobs);
		gui = new MainGUI();
		elevatorController = new Thread(new ElevatorController(1, jobs), "ElevatorController 1");
        elevator = new Thread(a, "Elevator 1");
        gui.addElevator(a);
	}
	
	@Test
	public void receiveRequests() throws IOException, InterruptedException {
		//Start elevator threads
		elevatorController.start();
		elevator.start();
		        
		//Receive elevator thread request for packet as if scheduler was receiving it
		byte data[] = new byte[3];
		requestPacket = new DatagramPacket(data, data.length);
		scheduler.receive(requestPacket);
		Assert.assertEquals(Arrays.toString(requestJob), Arrays.toString(requestPacket.getData()));
		        
		//Send task to elevator thread as if scheduler was sending it
		scheduler.send(job);
		//Let elevator thread run 
		Thread.sleep(500);
		while(a.getState().getCurrentState() != "IDLE") {
			gui.updateData();
			Thread.sleep(500);
		}
		
		//Receive elevator thread request for packet again
		requestPacket = new DatagramPacket(data, data.length);
		scheduler.receive(requestPacket);
		Assert.assertEquals(Arrays.toString(requestJob), Arrays.toString(requestPacket.getData()));
				
		//Send fault to elevator thread and make sure it is a fault 
		scheduler.send(faultJob);
		Assert.assertTrue(PacketHelper.convertPacketToRequest(faultJob).getFault());
		//Let elevator thread run showcasing what happens when a transient fault occurs
		Thread.sleep(500);
		while(a.getState().getCurrentState() != "IDLE") {
			gui.updateData();
			Thread.sleep(500);
		}
		
		//Check elevator completes and the job sent is no longer in elevator queue
		receivedJobs = a.getJobs();
		Assert.assertTrue(receivedJobs.size() == 0);
	}

}
