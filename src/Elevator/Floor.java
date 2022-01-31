package Elevator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;

public class Floor implements Runnable{
    private Scheduler scheduler;
    private int myFloor;
    private ArrayList<Request> incomingRequests;
    private int currentRequestIndex = 0;

    public Floor(Scheduler scheduler, int myFloor){
        this.scheduler = scheduler;
        this.myFloor = myFloor;
        this.incomingRequests = new ArrayList<>();
    }

    @Override
    public void run() {

        try (BufferedReader br = new BufferedReader(new FileReader("FloorCSV/floor_" + myFloor + ".csv"))) {
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

        synchronized (scheduler){
            while (true) {
                //System.out.println(LocalDateTime.now());
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                if(currentRequestIndex <= incomingRequests.size()) {
                    if (incomingRequests.get(currentRequestIndex).getTime().format(myFormatObj).equals(LocalDateTime.now().format(myFormatObj))) {
                        scheduler.putRequest(new Request[]{incomingRequests.get(currentRequestIndex)});
                        currentRequestIndex++;
                    }
                }
            }
        }
    }
}
