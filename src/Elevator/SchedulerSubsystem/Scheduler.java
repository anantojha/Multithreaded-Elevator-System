package Elevator.SchedulerSubsystem;

import Elevator.Enums.SchedulerStatus;
import Elevator.FloorSubsystem.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/*
    A class to handle the server portion of the app

    Class variables:
        threads: contains ServerThread objects - one per client that connects
        ss: ServerSocket object
 */
public class Scheduler implements Serializable
{
    ArrayList<ServerThread> floorThreads;
    ArrayList<ServerThread> elevatorThreads;
    ServerSocket ss;
    Queue<Request> queue;
    private SchedulerState state = new SchedulerState();
    public static AtomicInteger chosenElevator = new AtomicInteger(1);

    public static void main(String[] args) throws IOException {
        Scheduler chatSrv = new Scheduler();
        chatSrv.start();
    }

    public Scheduler() {
        this.floorThreads = new ArrayList<>();
        this.elevatorThreads = new ArrayList<>();
        this.queue = new ConcurrentLinkedQueue<>();
    }

    /* Starts server and accepts new connections */
    public void start() throws IOException {
        state.setState(SchedulerStatus.INITIALIZE);
        ss = new ServerSocket(10008);
        int counter = 0;
        try {
            while (true) {
                Socket socket = ss.accept();
                if (counter < 2){
                    ServerThread serverThread = new ServerThread(socket, floorThreads, elevatorThreads, state);
                    elevatorThreads.add(serverThread);
                    state.setState(SchedulerStatus.CONNECTELEVATOR);
                    System.out.println(state.getState());
                    serverThread.start();
                } else {
                    ServerThread serverThread = new ServerThread(socket, floorThreads, elevatorThreads, state);
                    floorThreads.add(serverThread);
                    state.setState(SchedulerStatus.CONNECTFLOOR);
                    System.out.println(state.getState());
                    serverThread.start();
                }
                counter = counter + 1;
            }
        } catch (Exception e) { }
    }

    /* Kills all ServerThread sockets and threads, then closes the ServerSocket */
    public void stop() throws IOException {
        for (ServerThread sT : this.elevatorThreads) {
            sT.socket.close();
            sT.interrupt();
        }
        for (ServerThread sT : this.floorThreads) {
            sT.socket.close();
            sT.interrupt();
        }
        ss.close();
    }

    public static class ServerThread extends Thread {
        private Socket socket;
        private ArrayList<ServerThread> floorThreadList;
        private ArrayList<ServerThread> elevatorThreadList;
        Queue<Request> requests;
        private ObjectInputStream dIn;
        private ObjectOutputStream dOut;
        int numRequests=0;
        SchedulerState state;

        public ServerThread(Socket socket, ArrayList<ServerThread> floorThreads, ArrayList<ServerThread> elevatorThreads, SchedulerState state) {
            this.socket = socket;
            this.floorThreadList = floorThreads;
            this.elevatorThreadList = elevatorThreads;
            this.requests = new ConcurrentLinkedQueue<>();
            this.state = state;
        }

        @Override
        public void run() {
            try {
                dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());
                SchedulerLoop();
            } catch (Exception e) {
                System.out.println("Error occurred: " + Arrays.toString(e.getStackTrace()));
            }
        }

        /* Waits for new messages to arrive, and broadcasts to all clients */
        private void SchedulerLoop() throws ClassNotFoundException {
            while (true) {

                try {
                    Request received = (Request) dIn.readObject();

                    if (received == null)
                        break;

                    // printToAllClients(outputString);
                    System.out.println("Server received: " + received);
                    System.out.println();
                    state.setState(SchedulerStatus.ADDINGREQUEST);
                    requests.add(received);

                    // send job to elevator
                    state.setState(SchedulerStatus.SENDREQUEST);
                    if(chosenElevator.get() == 1) {
                        elevatorThreadList.get(0).dOut.writeObject(requests.poll());
                        elevatorThreadList.get(0).dOut.flush();
                        chosenElevator.set(2);
                    } else if (chosenElevator.get() == 2) {
                        elevatorThreadList.get(1).dOut.writeObject(requests.poll());
                        elevatorThreadList.get(1).dOut.flush();
                        chosenElevator.set(1);
                    }
                    numRequests = 1 + numRequests;
                    state.setState(SchedulerStatus.COMPLETEREQUEST);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
