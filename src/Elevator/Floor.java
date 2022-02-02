package Elevator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;

/*
 * The Floor class represents the producer side of the algorithm. It is responsible for processing the csv files into incoming requests
 * These requests are then sent to the Scheduler if the time of the request matches the current time.
 * 
 */


public class Floor implements Runnable{
	
    private Scheduler scheduler;
    private int myFloor;
    private ArrayList<Request> incomingRequests;
    private int currentRequestIndex = 0;

    /*
	 * A constructor for the Floor class. The constructor initializes the shared data structure and sets what number of floor
	 * the thread is. 
	 * 
	 * Input: Schedule, int
	 * Output: none
	 * 
	 */
    public Floor(Scheduler scheduler, int myFloor){
        this.scheduler = scheduler;
        this.myFloor = myFloor;
        this.incomingRequests = new ArrayList<>();
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

    	//Process csv files into incoming requests
        try (BufferedReader br = new BufferedReader(new FileReader("CSV/FloorCSV/floor_" + myFloor + ".csv"))) {
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

        //Synchronize with scheduler
        synchronized (scheduler){
            for (Request r: incomingRequests) {
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                //If the incoming request time equals the current time, send a request to the scheduler
                while (!incomingRequests.get(currentRequestIndex).getTime().format(myFormatObj).equals(LocalDateTime.now().format(myFormatObj))) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                scheduler.putRequest(new Request[]{r});
            }
        }
        
    }
}
