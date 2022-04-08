package Elevator.ElevatorSubsystem;

import Elevator.FloorSubsystem.Request;
import Elevator.Global.PacketHelper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;


public class ElevatorController implements Runnable {
    private String Id;
    private DatagramSocket socket;
    private DatagramPacket sendPacket;
    public DatagramPacket receivePacket;
    private InetAddress localHostVar;
    byte[] taskRequest = {95, 1, 95};
    Queue<Request> jobs;

    /*
     * main(String [] args) asks user for elevator id and creates and starts elevator thread.
     *
     * Input: none
     * Output: none
     *
     */
    public static void main(String[] args) throws IOException {
        Queue<Request> jobs = new LinkedBlockingQueue<>();

        String id = askElevatorId();
        Thread elevatorController = new Thread(new ElevatorController(id, jobs), "ElevatorController " + id);
        Thread elevatorOne = new Thread(new Elevator(Integer.parseInt(id), jobs), "Elevator " + id);
        elevatorController.start();
        elevatorOne.start();

    }

    /*
     * The askElevatorId() method gets and returns user input for an elevator id.
     *
     * Input: none
     * Output: Returns user input elevator id as String
     *
     */
    public static String askElevatorId() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Elevator Id: ");
        String name = scanner.nextLine();
        return name;
    }

    /*
     * A constructor for the Elevator class. The constructor initializes the shared data structure and sets the id
     * of the Elevator. Each elevator starts from floor 1.
     *
     * Input:
     * id (String): The elevator id previously entered from askElevatorId() method
     *
     * Output: none
     *
     */
    public ElevatorController(String id, Queue<Request> jobs) throws IOException {
        //All elevators start at Floor 1
        this.Id = id;
        this.socket = new DatagramSocket(2950 + Integer.parseInt(id));
        socket.setSoTimeout(3000);
        localHostVar = InetAddress.getLocalHost();
        this.jobs = jobs;
    }

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
     * The receiveSchedulerTaskPacket() method is for send a request to the scheduler for a task.
     *
     * Input: none
     * Output: none
     *
     */
    public void sendSchedulerRequestPacket() throws IOException {
        // System.out.println("Elevator " + getId() + ": sending task request: " + Arrays.toString(taskRequest));
        sendPacket = new DatagramPacket(taskRequest, taskRequest.length, localHostVar, 2506);
        socket.send(sendPacket);
        // System.out.println("Elevator " + getId() + ": Packet Sent.");
    }

    private String getId() {
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
