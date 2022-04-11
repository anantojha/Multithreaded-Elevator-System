package Elevator.ElevatorSubsystem;

import Elevator.FloorSubsystem.Request;
import Elevator.Global.PacketHelper;
import Elevator.Global.SystemConfiguration;
import GUI.ControlPanelGUI;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public class ElevatorController implements Runnable {
    private int Id;
    private DatagramSocket socket;
    private DatagramPacket sendPacket;
    public DatagramPacket receivePacket;
    private InetAddress localHostVar;
    private static ControlPanelGUI gui;
    byte[] taskRequest = {95, 1, 95};
    Queue<Request> jobs;

    /*
     * main(String [] args) initializes the GUI, start the elevator controller and elevator threads
     *
     * Input: String[] args
     * Output: none
     *
     */
    public static void main(String[] args) throws IOException {
    	
    	try {
			gui = new ControlPanelGUI(SystemConfiguration.ELEVATORS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
        for(int i = 1; i < SystemConfiguration.ELEVATORS + 1; i++){
            Queue<Request> jobs = new LinkedBlockingQueue<>();
            Thread elevatorController = new Thread(new ElevatorController(i, jobs), "ElevatorController " + i);
            Thread elevatorOne = new Thread(new Elevator(i, jobs, gui ), "Elevator " + i);
            elevatorController.start();
            elevatorOne.start();
        }
        
    }

    /*
     * A constructor for the ElevatorController class initializes UDP socket for communication with scheduler.
     *
     * All elevators start at Floor 1
     *
     * Input: int id, Queue<Request> jobs
     *
     * Output: none
     *
     */
    public ElevatorController(int id, Queue<Request> jobs) throws IOException {
        this.Id = id;
        this.socket = new DatagramSocket(SystemConfiguration.ELEVATOR_PORT + id);
        socket.setSoTimeout(3000);
        localHostVar = InetAddress.getLocalHost();
        this.jobs = jobs;
    }

    /*
     * Run() for Elevator controller. Handles sending job request signal and receiving task data.
     * When a task is received, its added to the tasks queue.
     *
     */
    @Override
    public void run() {

        while (true) {

            try {
                sendSchedulerRequestPacket();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            try {
                receiveSchedulerTaskPacket();
                Request task = PacketHelper.convertPacketToRequest(receivePacket);
                jobs.add(task);
            } catch (SocketTimeoutException e) {
                //System.out.println("Elevator: Send timed out.");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /*
     * The receiveSchedulerTaskPacket() method is for sending a job request to the scheduler for a task.
     *
     * Input: none
     * Output: none
     *
     */
    public void sendSchedulerRequestPacket() throws IOException {
        // System.out.println("Elevator " + getId() + ": sending task request: " + Arrays.toString(taskRequest));
        sendPacket = new DatagramPacket(taskRequest, taskRequest.length, localHostVar, SystemConfiguration.SCHEDULER_ELEVATOR_PORT);
        socket.send(sendPacket);
        // System.out.println("Elevator " + getId() + ": Packet Sent.");
    }

    private int getId() {
        return Id;
    }

    /*
     * The receiveSchedulerTaskPacket() method is for receiving a Task from the scheduler.
     *
     * Input: none
     * Output: none
     *
     */
    public void receiveSchedulerTaskPacket() throws IOException {
        byte[] data = new byte[50];
        receivePacket = new DatagramPacket(data, data.length);
        socket.receive(receivePacket);
    }
}
