package Elevator.ElevatorSubsystem;

import Elevator.Enums.Direction;
import Elevator.Enums.ElevatorStatus;
import Elevator.FloorSubsystem.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.*;
import java.net.*;
import java.util.Scanner;

/*
 * The Elevator class represents the consumer side of the algorithm. It is responsible for accessing the requests sent to the scheduler
 * and fulfilling them given that the correct conditions are met.
 * 
 */
public class Elevator implements Runnable {

    private String Id;
    int initialFloor;
    private ElevatorState state;
    DatagramSocket socket;
    DatagramPacket request;
    int schedulerPortNum;
    
    /*
     * main(String [] args) asks user for elevator id and creates and starts elevator thread.
     * 
     * Input: none
     * Output: none
     * 
     */
    public static void main(String[] args) throws IOException {
        String id = askElevatorId();
        Thread elevatorOne = new Thread(new Elevator(id), "Elevator " + id);
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
    public Elevator(String id) throws IOException {
    	//All elevators start at Floor 1
        this.initialFloor = 1;
        this.Id = id;
        this.state = new ElevatorState(this.initialFloor);
        this.socket = new DatagramSocket();
        /*this.socket = new Socket("localhost", 10008);
        this.dOut = new ObjectOutputStream(socket.getOutputStream());
        this.dIn = new ObjectInputStream(socket.getInputStream());*/
    }

    /*
     * The getId() method returns the elevator id.
     * 
     * Input: none
     * Output: Return elevator id as String.
     * 
     */
    public String getId() {
        return Id;
    }
    
    /*
     * The getCurrentStatus() method returns the status the elevator is currently in.
     * 
     * Input: none
     * Output: Return elevator status as Enum ElevatorStatus.
     * 
     */
    public ElevatorStatus getCurrentStatus() {
        return state.getStatus();
    }
    
    /*
     * The updateState(ElevatorStatus status) method updates the current state of the Elevator and outputs the information to the console.
     * 
     * Input: 
     * status(ElevatorStatus): State elevator will be updated to.
     * 
     * Output: none
     * 
     */
    public void updateState(ElevatorStatus status) {
        state.setStatus(status);
        if (status.equals(ElevatorStatus.RUNNING)) {
            System.out.println("Elevator " + getId() + " " + getCurrentStatus() + " | " + state.getDirection());
        } else {
            System.out.println("Elevator " + getId() + " " + getCurrentStatus());
        }
    }

    /*
     * The run() method is the primary sequence that is run when a thread is active. In this case, the Elevator class will
     * attempt to receive requests from the scheduler and fulfill them if the request floor is the same as the thread floor.
     *
     * Input: none
     * Output: none
     *
     */

    @Override
    public void run() {
        try {
        	//connect Elevator thread to scheduler
        	schedulerPortNum = connectScheduler();
        	
            while (true) {
            	//Attempt to read request from Socket
            	//Object job = dIn.readObject();
            	Request job = null;
                byte [] received = new byte[10000];
                request = new DatagramPacket(received, received.length);
                socket.receive(request);
                byte data[] = new byte[request.getLength()];
                System.arraycopy(received, 0, data, 0, request.getLength());
                String data1 = new String(data);
                String dataContents[] = data1.split(" ");
                LocalDateTime datetime = LocalDateTime.parse(dataContents[2]);
                for (Direction d: Direction.values()) {
                	if (d.toString().equals(dataContents[8]))
                		job = new Request(datetime, Integer.parseInt(dataContents[5]), d, Integer.parseInt(dataContents[11]));
                }
                
                if (job == null)
                    continue;

                updateState(ElevatorStatus.IDLE);
                System.out.println();
                
                //Service the request read from Socket
                service(job);
            }
            // updateState(ElevatorStatus.TERMINATE);
        } catch (IOException e) { /*| ClassNotFoundException e) {*/
            e.printStackTrace();
        }
    }
    
    /*
     * move(int targetFloor) method moves elevator from currentFloor to the inputed floor
     * 
     * Input: 
     * floor int (serviceRequest.getDestinationFloor() or serviceRequest.getSourceFloor()): Floor number that the elevator will moving to.
     * 
     * Output: none
     * 
     */
    private void move(int targetFloor) {
    	try {
    		// Determine the direction the elevator will need to move 
        	boolean isDirectionUp = (state.getCurrentFloor() < targetFloor);
        	
        	// Set the direction state based on direction boolean above
        	if (isDirectionUp) {
        		state.setDirection(Direction.UP);
        	} else {
        		state.setDirection(Direction.DOWN);
        	}
           
        	// Move to the targetFloor 
        	updateState(ElevatorStatus.RUNNING);
        	for (int i = Math.abs(state.getCurrentFloor() - targetFloor); i > 0; i--) {
        		int x = isDirectionUp? 1 : -1; 	// If direction UP, increment current floor, else decrement
        		System.out.println(Thread.currentThread().getName() + " is at floor: " + state.getCurrentFloor());
        		state.setCurrentFloor(state.getCurrentFloor() + x);  // Increment or Decrement currentFloor
        		Thread.sleep(1000);
        	}
        	System.out.println(Thread.currentThread().getName() + " is at floor: " + state.getCurrentFloor());
        	
        	// Update state when approaching targetFloor
            updateState(ElevatorStatus.ARRIVED);
            Thread.sleep(600);
            updateState(ElevatorStatus.OPEN_DOOR);
            Thread.sleep(600);
        	
       	} catch (InterruptedException e) {
            e.printStackTrace();
        }   

    }
    
    /*
     * service(Request serviceRequest) method services the request received from the socket
     * 
     * Input: 
     * serviceRequest(Request): Request that the elevator will be completing.
     * 
     * Output: none
     * 
     */
    private void service(Request serviceRequest) {
    	try {
	        System.out.println(Thread.currentThread().getName() +" is servicing "+ serviceRequest);
	        
	        // Move to source floor
	        move(serviceRequest.getSourceFloor());
	        System.out.println(Thread.currentThread().getName() + " Pick up passengers");
	        updateState(ElevatorStatus.CLOSE_DOOR);
	        Thread.sleep(1400);
	        
	        // Move to destination floor floor
	        move(serviceRequest.getDestinationFloor());
	        System.out.println(Thread.currentThread().getName() + " Drop off passengers");
	        updateState(ElevatorStatus.CLOSE_DOOR);
	        Thread.sleep(1400);
	        
    	} catch (InterruptedException e) {
            e.printStackTrace();
        }   
    }
    

	private int connectScheduler() throws IOException {
    	//Send message to connect to scheduler
    	String message = "Connecting Elevator " + Id;
    	byte msg[] = message.getBytes();
    	request = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), 10010);
    	socket.send(request);
    	//Receive response from Scheduler's ServerThread
    	byte received[] = new byte[10000];
    	request = new DatagramPacket(received, received.length);
    	socket.receive(request);
    	byte data[] = new byte[request.getLength()];
        System.arraycopy(received, 0, data, 0, request.getLength());
        String data1 = new String(data);
        //return port number of Scheduler ServerThread connected to this Elevator thread
        if (data1 == "Connection Confirmed")
        	return request.getPort();
        return 0;
    }
}
