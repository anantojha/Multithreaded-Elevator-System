package Elevator.FloorSubsystem;

import Elevator.Enums.Direction;
import Elevator.Enums.FloorStatus;
import Elevator.Enums.SubSystemMapping;
import Elevator.Global.PacketHelper;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/*
 *  The Floor class represents the producer side of the algorithm. It creates requests coming from each floor thread in a csv file
 *  and reads the requests from the csv files and sends them to the scheduler once the current time is equal to the request time. 
 */
public class Floor implements Serializable, Runnable{

    private static Random random = new Random();
    private int myFloor;
    private ArrayList<Request> incomingRequests = new ArrayList<>();
    private DatagramSocket socket;
    private FloorState state;
    private DatagramPacket sendPacket;
    private InetAddress localHostVar;


    /*
     * main(String[] args) initializes and begins the floor threads.
     *
     * Input: none
     * Output: none
     *
     */
    public static void main(String[] args) throws IOException {
    	//create and start 10 floor threads
        createFloorCSV(11, "FloorCSV", 11);
        for(int i = 1; i < 11; i++){
            Thread floor = new Thread(new Floor(i), "Floor " + i);
            floor.start();
        }
    }


    /*
     * randomTimeDiff() returns a random long value in the range of 5 to 54
     * 
     * Input: none
     * Output: none
     * 
     */
    public static long randomTimeDiff(){
        return random.nextInt(7) + 7;
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
    public static void createFloorCSV(int numFloors, String folder, int numRequests) throws IOException {
        FileWriter csv = new FileWriter("CSV/" + folder + "/floor.csv");
        LocalTime timeCount = LocalDateTime.now().toLocalTime();
        for(int j = 0; j < numRequests; j++){
            int source;
            int destination;
            while (true){
                int randSource = random.nextInt(numFloors) + 1;
                int randDestination = random.nextInt(numFloors) + 1;
                if (randDestination != randSource){
                    source = randSource;
                    destination = randDestination;
                    break;
                }
            }
            
            //Time of request is random number of seconds after current time
            timeCount = timeCount.plusSeconds(randomTimeDiff());
            
            //Write all request information into csv file
            csv.append(timeCount.toString());
            csv.append(",");
            csv.append(String.valueOf(source));
            csv.append(",");
            if (destination > source)
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
        this.socket = new DatagramSocket(3080 + myFloor);
        localHostVar = InetAddress.getLocalHost();
        this.state = new FloorState();
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
            readCSV();
            sendReceiveRequest();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getMyFloor() {
        return myFloor;
    }


    /*
     * The readCSV() method processes a csv file corresponding to the floor thread that contains the incoming requests.
     * It will read the csv file and process the incoming requests into the incomingRequests arraylist.
     *
     * Input: None
     * Output: None
     */
    public void readCSV() {
        state.updateState();
        System.out.println(Thread.currentThread().getName() + ": updated state: " + state.getCurrentState());
        String fileToRead = "CSV/FloorCSV/floor.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(fileToRead))) {
            String line;
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

        incomingRequests.removeIf(r -> r.getSourceFloor() != this.getMyFloor());
    }

    /*
     * The getRequestList() is a getter method for retrieving the ArrayList of requests
     * the floor thread will send to the scheduler. 
     * 
     * Input: None
     * Output: ArrayList of Requests being sent to scheduler
     */
    public ArrayList<Request> getRequestList(){
    	return incomingRequests;
    }
    
    /*
     * The sendRequestToScheduler() method sends the request to the scheduler via. UDP
     *
     * Input: Request
     * Output: None
     */
    public void sendRequestToScheduler(Request r) throws IOException {
        state.updateState();
        System.out.println(Thread.currentThread().getName() + ": updated state: " + state.getCurrentState());
        byte[] packet = PacketHelper.buildRequestPacket(r);
        System.out.println(Thread.currentThread().getName() + ": sending request: " + r);
        System.out.println(Thread.currentThread().getName() + ": UDP Packet:" + Arrays.toString(packet));
        sendPacket = new DatagramPacket(packet, packet.length, localHostVar, 2505);
        socket.send(sendPacket);
        System.out.println(Thread.currentThread().getName() + ": Packet Sent.");
    }


    public void sendCoompletedSignal() throws IOException {
        state.updateState();
        System.out.println(Thread.currentThread().getName() + ": updated state: " + state.getCurrentState());
        byte[] packet = {0};
        System.out.println(Thread.currentThread().getName() + ": UDP Packet:" + Arrays.toString(packet));
        sendPacket = new DatagramPacket(packet, packet.length, localHostVar, 2505);
        socket.send(sendPacket);
        System.out.println(Thread.currentThread().getName() + ": Packet Sent.");
    }


    /*
     * The receiveResponseFromScheduler() method receives acknowledgement from the scheduler via. UDP
     *
     * Input: None
     * Output: None
     */
    public void receiveResponseFromScheduler() throws IOException {
        state.updateState();
        System.out.println(Thread.currentThread().getName() + ": updated state: " + state.getCurrentState());
        byte[] packet = new byte[1];
        sendPacket = new DatagramPacket(packet, packet.length);
        socket.receive(sendPacket);
        System.out.println(Thread.currentThread().getName() + ": Acknowledgement received: " + Arrays.toString(sendPacket.getData()));
        System.out.println();
    }


    /*
     * The sendReceiveRequest() method iterates through ArrayList of requests gotten from csv file earlier read from
     * and sends the request to the scheduler when the incoming request time is equal to the current time. Once
     * request is send, an acknowledgement response is received from scheduler.
     *
     * Input: None
     * Output: None
     */
    public void sendReceiveRequest() throws IOException, ClassNotFoundException {
        for (Request r: incomingRequests) {
            state.updateState();
            System.out.println(Thread.currentThread().getName() + ": updated state: " + state.getCurrentState());
            while(true){
                if(r.getTime().isBefore(LocalDateTime.now()))
                    break;
            }

            try {
                sendRequestToScheduler(r);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            try {
                receiveResponseFromScheduler();
            } catch (SocketTimeoutException e) {
                System.out.println("Client: Receive acknowledgement timed out.");
                System.out.println();
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
