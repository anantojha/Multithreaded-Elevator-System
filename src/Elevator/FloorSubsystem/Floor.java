package Elevator.FloorSubsystem;

import Elevator.Enums.Direction;
import Elevator.Enums.FloorStatus;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

/*
 *  The Floor class represents the producer side of the algorithm. It creates requests coming from each floor thread in a csv file
 *  and reads the requests from the csv files and sends them to the scheduler once the current time is equal to the request time. 
 */
public class Floor implements Serializable, Runnable{

    private static Random random = new Random();
    private int myFloor;
    private ArrayList<Request> incomingRequests = new ArrayList<>();
    Socket socket;
    private ObjectInputStream dIn;
    private ObjectOutputStream dOut;
    private FloorState state;

    /*
     * main(String[] args) initializes and begins the floor threads.
     *
     * Input: none
     * Output: none
     *
     */
    public static void main(String[] args) throws IOException {
    	//create and start 10 floor threads
        Thread floorOne = new Thread(new Floor(1), "Floor 1");
        Thread floorTwo = new Thread(new Floor(2),"Floor 2");
        Thread floorThree = new Thread(new Floor(3),"Floor 3");
        Thread floorFour = new Thread(new Floor(4),"Floor 4");
        Thread floorFive = new Thread(new Floor(5),"Floor 5");

        Thread floorSix = new Thread(new Floor(6), "Floor 6");
        Thread floorSeven = new Thread(new Floor(7),"Floor 7");
        Thread floorEight = new Thread(new Floor(8),"Floor 8");
        Thread floorNine = new Thread(new Floor(9),"Floor 9");
        Thread floorTen = new Thread(new Floor(10),"Floor 10");

        floorOne.start();
        floorTwo.start();
        floorThree.start();
        floorFour.start();
        floorFive.start();

        floorSix.start();
        floorSeven.start();
        floorEight.start();
        floorNine.start();
        floorTen.start();
    }
    
    /*
     * randomTimeDiff() returns a random long value in the range of 5 to 54
     * 
     * Input: none
     * Output: none
     * 
     */
    public static long randomTimeDiff(){
        return random.nextInt(50) + 5;
    }


    /*
     * createFloorCSV(int floors) creates a csv file for some number of floors specified with the following structure:
     * [Time, floor, floor button, car button]
     *
     * Input: 
     * floor(int) : The floor request(s) will be generated for
     * folder(String) : The folder to store the csv file
     * numRequests(int) : The number of requests to generate for the floor
     * 
     * Output: none
     * 
     */
    public static void createFloorCSV(int floor, String folder, int numRequests) throws IOException {
        FileWriter csv = new FileWriter("CSV/" + folder + "/floor_" + floor + ".csv");
        LocalTime timeCount = LocalDateTime.now().toLocalTime();
        for(int j = 0; j < numRequests; j++){
            int destination;
            while (true){
                int randDestination = random.nextInt(10) + 1;
                if (randDestination != floor){
                    destination = randDestination;
                    break;
                }
            }
            
            //Time of request is random number of seconds after current time
            timeCount = timeCount.plusSeconds(randomTimeDiff());
            
            //Write all request information into csv file
            csv.append(timeCount.toString());
            csv.append(",");
            csv.append(String.valueOf(floor));
            csv.append(",");
            if (destination > floor) 
            	csv.append("UP");
            else 
            	csv.append("DOWN");
            csv.append(",");
            csv.append(destination + "");
            csv.append("\n");
        }
        csv.flush();
        csv.close();
    }


    /*
     * A constructor for the Floor class. The constructor initializes the shared data structure and sets what number of floor
     * the thread is.
     *
     * Input: 
     * myFloor(int): The floor the thread will represent as (1-10).
     * 
     * Output: none
     *
     */
    public Floor(int myFloor) throws IOException {
        this.myFloor = myFloor;
        this.socket = new Socket("localhost", 10008);
        this.dOut = new ObjectOutputStream(socket.getOutputStream());
        this.dIn = new ObjectInputStream(socket.getInputStream());
        //this.random = new Random();
        this.state = new FloorState(FloorStatus.INITIALIZE);
    }


    /*
     * The run() method is the primary sequence that is run when a thread is active. In this case, the Floor class will
     * attempt to process all local csv files into incoming requests. These requests are then later sent to the Scheduler
     * if the given conditions are correct (time).
     *
     * Input: none
     * Output: none
     *
     */
    @Override
    public void run() {
        try {
        	//Create csv file for each floor thread with 2 request
            createFloorCSV(myFloor , "FloorCSV", 2);

            //Read CSV file for this floor
            readCSV();

            //Send request to scheduler when request time is same as current time
            sendRequest();
            dIn.close();
            dOut.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /*
     * The readCSV() method processes a csv file corresponding to the floor thread that contains the incoming requests.
     * It will read the csv file and process the incoming requests into the incomingRequests arraylist.
     *
     * Input: None
     * Output: None
     */
    public void readCSV() {
        state.setState(FloorStatus.PROCESSING);
        String fileToRead = "CSV/FloorCSV/floor_" + myFloor + ".csv";

        //Process csv files into incoming requests
        try (BufferedReader br = new BufferedReader(new FileReader(fileToRead))) {
            String line;
            //For each line in the csv, find the appropriate Direction, and add it to the request
            while ((line = br.readLine()) != null) {
                String[] requestContents = line.split(",");
                for(Direction d: Direction.values()){
                    if(d.toString().equals(requestContents[2])){
                        LocalDate date = LocalDateTime.now().toLocalDate();
                        incomingRequests.add(new Request(LocalDateTime.parse(date.toString() + "T" + requestContents[0]), Integer.parseInt(requestContents[1]),
                                d, Integer.parseInt(requestContents[3])));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * The sendRequest() method iterates through ArrayList of requests gotten from csv file earlier read from
     * and sends the request to the scheduler when the incoming request time is equal to the current time.
     *
     * Input: None
     * Output: None
     */
    public void sendRequest() throws IOException, ClassNotFoundException {
        for (Request r: incomingRequests) {
            while(true){
                state.setState(FloorStatus.WAITING);
                if(r.getTime().isBefore(LocalDateTime.now()))
                    break;
            }
            state.setState(FloorStatus.SENDING);
            System.out.println(Thread.currentThread().getName() + " sent request: " + r);
            dOut.writeObject(r);
            dOut.flush();
        }
        dOut.writeObject(null);
    }
}
