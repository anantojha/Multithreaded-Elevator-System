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

public class Scheduler implements Runnable {

    DatagramPacket receiveFloorPacket, receiveServerPacket, sendReplyPacket;
    DatagramSocket elevatorSocket, floorSocket;
    private byte[] acknowledgementSignal = {1};
    private Queue<byte[]> tasks;
    private Random random = new Random();

    /**
     * IntermediateHost Constructor for the class.
     */
    public Scheduler() {
        try {
            tasks = new LinkedBlockingQueue<>();
            floorSocket = new DatagramSocket(2505);
            elevatorSocket = new DatagramSocket(2506);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    private int getEstimateTripTime(Request r){
        int estimatedTripTime = 100000;
        return estimatedTripTime;
    }

    /*
     * The elevatorHandle() method is for handling communication with elevators.
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

            int elevatorId = receiveServerPacket.getData()[1];

            if(!tasks.isEmpty()){
                try {
                    byte[] taskToSend = tasks.poll();
                    sendReplyPacket = new DatagramPacket(taskToSend, taskToSend.length,
                            InetAddress.getLocalHost(), 2951+ random.nextInt(SystemConfiguration.ELEVATORS));
                    System.out.println("Scheduler: sending task ["+ PacketHelper.convertPacketToRequest(sendReplyPacket) +"] to Elevator  (" + receiveServerPacket.getAddress() + ":" + receiveServerPacket.getPort() +")...\n");
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
     * The floorHandle() method is for handling communication with floors.
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

    public static void main(String args[]) throws InterruptedException {
        Scheduler c = new Scheduler();
        Thread client = new Thread(c, "Client Thread");
        client.start();

        while (true) {
            c.elevatorHandle();
        }
    }
}

