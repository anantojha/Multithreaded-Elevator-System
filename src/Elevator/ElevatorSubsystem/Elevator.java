package Elevator.ElevatorSubsystem;

import Elevator.Enums.Direction;
import Elevator.Enums.ElevatorStatus;
import Elevator.FloorSubsystem.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/*
 * The Elevator class represents the consumer side of the algorithm. It is responsible for accessing the requests sent to the scheduler
 * and fulfilling them given that the correct conditions are met.
 * 
 */
public class Elevator implements Runnable {

    private String Id;
    Socket socket;
    private ObjectInputStream dIn;
    private ObjectOutputStream dOut;
    int initialFloor;
    private ElevatorState state;
    
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
        this.socket = new Socket("localhost", 10008);
        this.dOut = new ObjectOutputStream(socket.getOutputStream());
        this.dIn = new ObjectInputStream(socket.getInputStream());
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

            while (true) {
            	//Attempt to read request from Socket
                Object job = dIn.readObject();

                if (job == null)
                    continue;

                updateState(ElevatorStatus.IDLE);
                System.out.println();
                
                //Service the request read from Socket
                service((Request) job);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * service(Request serviceRequest) method services the request received from the socket
     */
    private void service(Request serviceRequest) {

        System.out.println(Thread.currentThread().getName() +" is servicing "+ serviceRequest);

        // Move to source floor if not already on source floor
        if (serviceRequest.getSourceFloor() != state.getCurrentFloor()) {
            try {
            	//If Elevator is above Request floor
                if (state.getCurrentFloor() > serviceRequest.getSourceFloor()) {
                    state.setDirection(Direction.DOWN);
                    for (int i = (state.getCurrentFloor() - serviceRequest.getSourceFloor()); i > 0; i--) {
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + " is at floor: " + state.getCurrentFloor());
                        state.setCurrentFloor(state.getCurrentFloor() - 1);
                    }
                    state.setCurrentFloor(serviceRequest.getSourceFloor());
                //If Elevator is below Request floor
                } else if (state.getCurrentFloor() < serviceRequest.getSourceFloor()) {
                    state.setDirection(Direction.UP);
                    for (int i = (serviceRequest.getSourceFloor() - state.getCurrentFloor()); i > 0; i--) {
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + " is at floor: " + (state.getCurrentFloor()));
                        state.setCurrentFloor(state.getCurrentFloor() + 1);
                    }
                    state.setCurrentFloor(serviceRequest.getSourceFloor());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Move from Request source floor to destination floor
        try {
        	//If source floor is below destination floor
            if (serviceRequest.getDestinationFloor() > serviceRequest.getSourceFloor()) {
                state.setDirection(Direction.UP);
                for (int i = (serviceRequest.getDestinationFloor() - serviceRequest.getSourceFloor()); i > 0; i--) {
                	//If elevator is at source floor
                    if (state.getCurrentFloor() == serviceRequest.getSourceFloor()){
                        System.out.println(Thread.currentThread().getName() + " is at floor: " + (state.getCurrentFloor()) + " - pickup");
                        Thread.sleep(1000);
                        updateState(ElevatorStatus.ARRIVED);
                        Thread.sleep(600);
                        updateState(ElevatorStatus.OPEN_DOOR);
                        Thread.sleep(600);
                        updateState(ElevatorStatus.CLOSE_DOOR);
                        updateState(ElevatorStatus.RUNNING);
                        Thread.sleep(1400);

                    } else { //If elevator is moving towards destination floor
                        System.out.println(Thread.currentThread().getName() + " is at floor: " + (state.getCurrentFloor()));
                        Thread.sleep(1000);
                    }
                    state.setCurrentFloor(state.getCurrentFloor() + 1);
                }
                //Output once elevator is at destination floor 
                System.out.println(Thread.currentThread().getName() + " is at floor: " + (state.getCurrentFloor()) + " - dropoff");
                updateState(ElevatorStatus.ARRIVED);
                Thread.sleep(600);
                updateState(ElevatorStatus.OPEN_DOOR);
                Thread.sleep(600);
                updateState(ElevatorStatus.CLOSE_DOOR);
                state.setCurrentFloor(serviceRequest.getSourceFloor());
            //If source floor is above destination floor 
            } else {
                state.setDirection(Direction.DOWN);
                for (int i = (serviceRequest.getSourceFloor() - serviceRequest.getDestinationFloor()); i > 0; i--) {
                	//If elevator is at source floor
                    if(state.getCurrentFloor() == serviceRequest.getSourceFloor()){
                        System.out.println(Thread.currentThread().getName() + " is at floor: " + (state.getCurrentFloor()) + " - pickup");
                        Thread.sleep(1000);
                        updateState(ElevatorStatus.ARRIVED);
                        Thread.sleep(600);
                        updateState(ElevatorStatus.OPEN_DOOR);
                        Thread.sleep(600);
                        updateState(ElevatorStatus.CLOSE_DOOR);
                        updateState(ElevatorStatus.RUNNING);
                        Thread.sleep(1400);
                    } else { //If elevator is moving towards destination floor 
                        System.out.println(Thread.currentThread().getName() + " is at floor: " + (state.getCurrentFloor()));
                        Thread.sleep(1000);
                    }
                    state.setCurrentFloor(state.getCurrentFloor() - 1);
                }
                //Output once elevator is at destination floor
                System.out.println(Thread.currentThread().getName() + " is at floor: " + (state.getCurrentFloor()) + " - dropoff");
                updateState(ElevatorStatus.ARRIVED);
                Thread.sleep(600);
                updateState(ElevatorStatus.OPEN_DOOR);
                Thread.sleep(600);
                updateState(ElevatorStatus.CLOSE_DOOR);

                //Update current floor to destination
                state.setCurrentFloor(serviceRequest.getDestinationFloor());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

