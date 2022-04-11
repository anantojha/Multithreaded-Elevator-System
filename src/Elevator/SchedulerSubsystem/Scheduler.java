package Elevator.SchedulerSubsystem;

import Elevator.FloorSubsystem.Request;
import Elevator.Global.PacketHelper;
import Elevator.Global.SystemConfiguration;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

/*
 * The scheduler class is a runnable thread that is responsible for managing the UDP requests between floor and elevator.
 * 
 */
public class Scheduler implements Runnable {

    DatagramPacket receiveFloorPacket, receiveServerPacket, sendReplyPacket;
    DatagramSocket elevatorSocket, floorSocket;
    private final byte[] acknowledgementSignal = {SystemConfiguration.ACKNOWLEDGEMENT_SIGNAL};
    private Queue<byte[]> tasks;
    private int lastElevator = 1;

    /*
     * Scheduler constructor, create blocking queue of incoming and outgoing communication data.
     * Datagram Sockets for initialized for floor and elevator subsystems. Intermediate host.
     *
     * input: none
     * output: none
     *
     */
    public Scheduler() {
        try {
            tasks = new LinkedBlockingQueue<>();
            floorSocket = new DatagramSocket(SystemConfiguration.SCHEDULER_FLOOR_PORT);
            elevatorSocket = new DatagramSocket(SystemConfiguration.SCHEDULER_ELEVATOR_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /*
     * The elevatorHandle() method is for handling communication with elevator controller.
     * Initially the scheduler is waiting for an elevator to send a job request signal. Once
     * the signal is received, a response packet containing task data is sent from the tasks queue.
     *
     *
     * Input: none
     * Output: none
     *
     */
    public void elevatorHandle() throws InterruptedException {

        byte data[] = new byte[50];
        receiveServerPacket = new DatagramPacket(data, data.length);
        int estimatedTime = 100000;

        System.out.println("Scheduler: Waiting for Elevator Request...\n");
        try {
            elevatorSocket.receive(receiveServerPacket);
        } catch (SocketTimeoutException e) {
            System.out.println("Scheduler: Fault detected.");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        if(!tasks.isEmpty()){
            try {
                byte[] taskToSend = tasks.poll();
                int elevatorChoice = SystemConfiguration.ELEVATOR_PORT + getNextElevator();
                sendReplyPacket = new DatagramPacket(taskToSend, taskToSend.length,
                        InetAddress.getLocalHost(), elevatorChoice);
                System.out.println("Scheduler: sending task ["+ PacketHelper.convertPacketToRequest(sendReplyPacket) +"] to Elevator  (" + InetAddress.getLocalHost() + ":" + elevatorChoice +")...\n");
                elevatorSocket.send(sendReplyPacket);
                elevatorSocket.setSoTimeout(estimatedTime);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * The getNextElevator() method is routing udp to the next elevator.
     *
     * Input: none
     * Output: none
     *
     */
    public int getNextElevator() {
        int temp = lastElevator;
        lastElevator++;
        if(temp == SystemConfiguration.ELEVATORS){
            lastElevator = 1;
        }
        return temp;
    }

    /*
     * The floorHandle() method is for handling communication with floors. Initially the scheduler is
     * waiting for a floor to send an incoming request. When a request is received the floor is send an
     * acknowledgement signal.
     *
     * Input: none
     * Output: none
     *
     */
    public void floorHandle(){
        byte data[] = new byte[50];
        receiveFloorPacket = new DatagramPacket(data, data.length);
        int counter = 0;

        while (true) {
            System.out.println("Scheduler: Waiting for Floor Request...\n");
            try {
                floorSocket.receive(receiveFloorPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            if (receiveFloorPacket.getData()[0] == 0) {
                break;
            }

            System.out.println("Scheduler: adding new task to Queue: " + Arrays.toString(receiveFloorPacket.getData()));
            tasks.add(receiveFloorPacket.getData());
            counter++;

            System.out.println("Scheduler: sending acknowledgement to Floor ("+ receiveFloorPacket.getAddress() + ":" + receiveFloorPacket.getPort() + ")...\n");
            try {
                sendReplyPacket = new DatagramPacket(acknowledgementSignal, acknowledgementSignal.length,
                        InetAddress.getLocalHost(), receiveFloorPacket.getPort());
                floorSocket.send(sendReplyPacket);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(counter == 20){
                break;
            }
        }
    }
    
    /*
     * getQueue() is a getter method for getting the tasks the floor sends
     * for the elevator to service.
     * 
     * Input: None
     * Output: Queue of tasks
     */
    public Queue<byte[]> getQueue() {
    	return tasks;
    }
    
    @Override
    public void run() {
        while (true) {
            floorHandle();
        }
    }

    /*
     * main() function to start the scheduler. Run First.
     *
     * Input: String args[]
     * Output: Queue of tasks
     */
    public static void main(String args[]) throws InterruptedException {
        Scheduler c = new Scheduler();
        Thread client = new Thread(c, "Client Thread");
        client.start();

        while (true) {
            c.elevatorHandle();
        }
    }
}

