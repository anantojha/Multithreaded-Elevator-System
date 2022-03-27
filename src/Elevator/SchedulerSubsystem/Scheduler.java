package Elevator.SchedulerSubsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Scheduler implements Runnable {

    DatagramPacket receiveFloorPacket, receiveServerPacket, sendReplyPacket;
    DatagramSocket elevatorSocket, floorSocket;
    private byte[] acknowledgementSignal = {1};
    private Queue<byte[]> tasks;

    /**
     * IntermediateHost Constructor for the class.
     */
    public Scheduler() {
        try {
            tasks = new ConcurrentLinkedQueue<>();
            floorSocket = new DatagramSocket(2505);
            elevatorSocket = new DatagramSocket(2506);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /*
     * The elevatorHandle() method is for handling communication with elevators.
     *
     * Input: none
     * Output: none
     *
     */
    public void elevatorHandle(){

        byte data[] = new byte[50];
        receiveServerPacket = new DatagramPacket(data, data.length);

        while (true) {
            System.out.println("Scheduler: Waiting for Elevator Request...\n");
            try {
                elevatorSocket.receive(receiveServerPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            int elevatorId = receiveServerPacket.getData()[1];
            if(!tasks.isEmpty()){
                System.out.println("Scheduler: sending task to Elevator "+ elevatorId + " (" + receiveServerPacket.getAddress() + ":" + receiveServerPacket.getPort() +")...\n");
                try {
                    byte[] taskToSend = tasks.poll();
                    sendReplyPacket = new DatagramPacket(taskToSend, taskToSend.length,
                            InetAddress.getLocalHost(), receiveServerPacket.getPort());
                    elevatorSocket.send(sendReplyPacket);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

        while (true) {
            System.out.println("Scheduler: Waiting for Floor Request...\n");
            try {
                floorSocket.receive(receiveFloorPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.out.println("Scheduler: adding new task to Queue: " + Arrays.toString(receiveFloorPacket.getData()));
            tasks.add(receiveFloorPacket.getData());

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
        }
    }

    @Override
    public void run() {
        while (true) {
            floorHandle();
        }
    }

    public static void main(String args[]) {
        Scheduler c = new Scheduler();
        Thread client = new Thread(c, "Client Thread");
        client.start();

        while (true) {
            c.elevatorHandle();
        }
    }
}

