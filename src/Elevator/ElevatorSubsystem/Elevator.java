package Elevator.ElevatorSubsystem;

import Elevator.Enums.Direction;
import Elevator.Enums.ElevatorStatus;
import Elevator.FloorSubsystem.Request;
import Elevator.Global.PacketHelper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

/*
 * The Elevator class represents the consumer side of the algorithm. It is responsible for accessing the requests sent to the scheduler
 * and fulfilling them given that the correct conditions are met.
 * 
 */
public class Elevator implements Runnable {
	
	final private float avgtime = 1.347887712f;

    private String Id;
    private DatagramSocket socket;
    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;
    private InetAddress localHostVar;
    int initialFloor;
    private ElevatorState state;
    byte[] taskRequest = {95, 1, 95};
	Timer timer;

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
        this.socket = new DatagramSocket(2950);
        socket.setSoTimeout(3000);
        localHostVar = InetAddress.getLocalHost();
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
     * The receiveSchedulerTaskPacket() method is for send a request to the scheduler for a task.
     *
     * Input: none
     * Output: none
     *
     */
    public void sendSchedulerRequestPacket() throws IOException {
        System.out.println("Elevator " + getId() + ": sending task request: " + Arrays.toString(taskRequest));
        sendPacket = new DatagramPacket(taskRequest, taskRequest.length, localHostVar, 2506);
        socket.send(sendPacket);
        System.out.println("Elevator " + getId() + ": Packet Sent.");
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

    /*
     * The run() method is the primary sequence that is run when a thread is active. In this case, the Elevator class will
     * attempt to send requests to the scheduler for a Task and then receive a response from the scheduler.
     *
     * Input: none
     * Output: none
     *
     */
    @Override
    public void run() {

        while (true) {
            updateState(ElevatorStatus.IDLE);
            System.out.println();
			float initial = System.nanoTime()/1000000000;

            try {
                sendSchedulerRequestPacket();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            try {
                receiveSchedulerTaskPacket();
            } catch (SocketTimeoutException e) {
                System.out.println("Elevator: Send timed out.");
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            Request task = PacketHelper.convertPacketToRequest(receivePacket);
            service(task);
			float finale = System.nanoTime()/1000000000;
			System.out.println(finale-initial);

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
    		int initialFloor = state.getCurrentFloor();
    		//Create a timer
    		timer = new Timer();
    		timer.schedule(new TimerTask() {
    			public void run() {
    				//When the timer ends, and the elevator is not at their target floor, exit. Otherwise, continue.
    				if(state.getCurrentFloor() == targetFloor) {
    					return;
    				}else {
    					System.out.println("Fault has been detected | Timer was " + (Math.abs(initialFloor - targetFloor) * avgtime));
    					System.exit(1);
    				}
    			}
    		}, (long) (Math.abs(initialFloor - targetFloor) * avgtime * 1000));
    		
            // Check if elevator is already at target floor
            if (state.getCurrentFloor() == targetFloor){
                return;
            }

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
	        
	        // Move to destination floor
	        move(serviceRequest.getDestinationFloor());
	        System.out.println(Thread.currentThread().getName() + " Drop off passengers");
	        updateState(ElevatorStatus.CLOSE_DOOR);
	        Thread.sleep(1400);
	        
    	} catch (InterruptedException e) {
            e.printStackTrace();
        }   
    }
}
