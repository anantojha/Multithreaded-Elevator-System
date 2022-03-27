package Elevator.SchedulerSubsystem;

import Elevator.Enums.Direction;
import Elevator.Enums.SchedulerStatus;
import Elevator.FloorSubsystem.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
import java.time.LocalDateTime;
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
        queue: Queue of requests received from Floor threads
 */
public class Scheduler implements Serializable
{
    ArrayList<ServerThread> floorThreads;
    ArrayList<ServerThread> elevatorThreads;
    DatagramSocket socket;
    DatagramPacket connection;
    //ServerSocket ss;
    Queue<Request> queue;
    private SchedulerState state = new SchedulerState();
    public static AtomicInteger chosenElevator = new AtomicInteger(1);
    
    /*
     * main(String[] args) initializes and starts scheduler instance.
     * 
     * Input: none
     * Output: none
     * 
     */
    public static void main(String[] args) throws IOException {
        Scheduler Srv = new Scheduler();
        Srv.start();
    }
    
    /*
     * A constructor for the Scheduler class. The constructor initializes the ArrrayLists of ServerThreaads for floorThreads
     * and elevatorThreads. Constructor also initializes the queue for all requests it receives from Floor threads.
     * 
     * Input: none
     * Output: none
     * 
     */
    public Scheduler() {
        this.floorThreads = new ArrayList<>();
        this.elevatorThreads = new ArrayList<>();
        this.queue = new ConcurrentLinkedQueue<>();
        try {
			this.socket = new DatagramSocket(201);
		} catch (SocketException e) {
			e.printStackTrace();
		}
    }			

    /* 
     * The start() method starts the Scheduler instance by accepting 2 elevator thread connections 
     * and any number of floor thread connections. For each connection a ServerThread is created and started.
     * 
     *  Input: none
     *  Output: none
     *  
     */
    public void start() throws IOException {
        state.setState(SchedulerStatus.INITIALIZE);
        //ss = new ServerSocket(10008);
        int counter = 0;
        try {
            while (true) {
                //Socket socket = ss.accept();
                //create and start 2 ServerThreads for the 2 Elevator threads
            	byte [] received = new byte[10000];
                connection = new DatagramPacket(received, received.length);
                socket.receive(connection);
                byte data[] = new byte[connection.getLength()];
                System.arraycopy(received, 0, data, 0, connection.getLength());
                String data1 = new String(data);
                ServerThread serverThread = null;
                if (!data1.equals(null)) 
                	serverThread = new ServerThread(connection.getPort(), floorThreads, elevatorThreads, state, queue);
                if (counter < 2 ){
                    //ServerThread serverThread = new ServerThread(socket, floorThreads, elevatorThreads, state, queue);
                    elevatorThreads.add(serverThread);
                    state.setState(SchedulerStatus.CONNECTELEVATOR);
                    System.out.println(state.getState());
                    serverThread.start();
                } else { //create and start a ServerThread for each Floor thread
                    //ServerThread serverThread = new ServerThread(socket, floorThreads, elevatorThreads, state, queue);
                    floorThreads.add(serverThread);
                    state.setState(SchedulerStatus.CONNECTFLOOR);
                    System.out.println(state.getState());
                    serverThread.start();
                }
                counter = counter + 1;
            }
        } catch (Exception e) { }
    }

    // getter for requests queue
    public Queue<Request> getQueue() {
        return queue;
    }

    /*
     *  The stop() method kills all ServerThread sockets and threads created from start() method, 
     *  then closes the ServerSocket.
     *  
     *   Input: none
     *   Output: none
     *   
     */
    public void stop() throws IOException {
    	//Close all sockets and ServerThreads created for Elevator threads
        for (ServerThread sT : this.elevatorThreads) {
            sT.socket.close();
            sT.interrupt();
        }
        //Close all sockets and ServerThreads created for Floor threads
        for (ServerThread sT : this.floorThreads) {
            sT.socket.close();
            sT.interrupt();
        }
        socket.close();
        //ss.close();
    }
    
    /*
     * A class used by Scheduler to create a thread for each connection with Elevator and Floor threads.
     * The ServerThread class will perform the Scheduler's intended operations such as receiving requests from Floor threads,
     * and sending requests to Elevator threads.
     * 
     * 	Class variables:
     * 		threads: contains ServerThread objects - one per client that connects
     * 		socket: Socket object 
     * 		requests: Queue of requests received from Floor threads
     */
    public static class ServerThread extends Thread {
        //private Socket socket;
    	private DatagramSocket socket;
    	private DatagramPacket request;
    	private int socketPort;
        private ArrayList<ServerThread> floorThreadList;
        private ArrayList<ServerThread> elevatorThreadList;
        Queue<Request> requests;
        private ObjectInputStream dIn;
        private ObjectOutputStream dOut;
        int numRequests=0;
        SchedulerState state;
        
        /*
         * A constructor for the ServerThread class. The constructor initializes the socket object, ArrayLists of ServerThreads
         * for floorThreads and elevatorThreads, Queue of requests, and the Scheduler's state.
         * 
         *  Input:
         *  socket(Socket): The ServerSocket object passed by Scheduler instance
         *  floorThreads: Arraylist of Floor threads connected to Scheduler
         *  elevatorThreads: Arraylist of Elevator threads connected to Scheduler
         *  state: State of scheduler 
         */
        public ServerThread(int socketPort, ArrayList<ServerThread> floorThreads, ArrayList<ServerThread> elevatorThreads, SchedulerState state, Queue<Request> queue) {
            try {
				this.socket = new DatagramSocket();
			} catch (SocketException e) {
				e.printStackTrace();
			}
            this.socketPort = socketPort;
            this.floorThreadList = floorThreads;
            this.elevatorThreadList = elevatorThreads;
            this.requests = queue;
            this.state = state;
        }
        
        /*
         * The run() method is the primary sequence that is run when a thread is active. For the ServerThread class it will
         * initialize the input and output streams for the thread, then attempt to receive requests from the socket and send 
         * the request to a elevator thread through the socket. 
         * 
         * Input: none
         * Output: none
         * 
         */
        @Override
        public void run() {
            try {
                /*dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());*/
            	confirmConnection();
                SchedulerLoop();
            } catch (Exception e) {
                System.out.println("Error occurred: " + Arrays.toString(e.getStackTrace()));
            }
        }
        
        private void confirmConnection() {
        	try {
        		String message = "Connection Confirmed";
        		byte msg[] = message.getBytes();
        		request = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), socketPort);
        		socket.send(request);
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
        /* 
         * The SchedulerLoop() method attempts to receive requests from the socket, once a request is received it is 
         * output to the console and added to the requests queue. After receiving a request, the method sends the 
         * request to an available elevator. 
         * 
         *  Input: none
         *  Output: none
         *  
         */
        private void SchedulerLoop() throws ClassNotFoundException {
            while (true) {

                try {
                	//Request received = (Request) dIn.readObject();
                    Request received = null; 
                    byte [] receivedData = new byte[1000];
                    request = new DatagramPacket(receivedData, receivedData.length);
                    socket.receive(request);
                    byte data[] = new byte[request.getLength()];
                    System.arraycopy(receivedData, 0, data, 0, request.getLength());
                    String data1 = new String(data);
                    String dataContents[] = data1.split(" ");
                    LocalDateTime datetime = LocalDateTime.parse(dataContents[2]);
                    for (Direction d: Direction.values()) {
                    	if (d.toString().equals(dataContents[8]))
                    		received = new Request(datetime, Integer.parseInt(dataContents[5]), d, Integer.parseInt(dataContents[11]));
                    }
                    
                    if (received == null)
                        break;
                    
                    // printToAllClients(outputString);
                    System.out.println("Server received: " + received);
                    System.out.println();
                    state.setState(SchedulerStatus.ADDINGREQUEST);
                    requests.add(received);

                    DatagramPacket job;
                    byte msg[] = requests.poll().toString().getBytes();
                    // send job to elevator
                    state.setState(SchedulerStatus.SENDREQUEST);
                	
                    if(chosenElevator.get() == 1) {
                    	job = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), elevatorThreadList.get(0).socketPort);
                    	socket.send(job);
                        /*elevatorThreadList.get(0).dOut.writeObject(requests.poll());
                        elevatorThreadList.get(0).dOut.flush();*/
                        chosenElevator.set(2);
                    } else if (chosenElevator.get() == 2) {
                    	job = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), elevatorThreadList.get(1).socketPort);
                    	socket.send(job);
                        /*elevatorThreadList.get(1).dOut.writeObject(requests.poll());
                        elevatorThreadList.get(1).dOut.flush();*/
                        chosenElevator.set(1);
                    }
                    numRequests = 1 + numRequests;
                    state.setState(SchedulerStatus.COMPLETEREQUEST);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // getter for requests queue
        public Queue<Request> getRequests() {
            return requests;
        }
    }
}
