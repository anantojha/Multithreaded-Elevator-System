package Elevator;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        createFloorCSV(2);
        Thread.sleep(100);

        Scheduler scheduler = new Scheduler();

        Thread floorOne = new Thread(new Floor(scheduler, 1), "Elevator.Floor 1");
        floorOne.start();

    }


    private static void createFloorCSV(int floors) throws IOException {
        for(int i = 1; i <= floors; i++){
            FileWriter csv = new FileWriter("FloorCSV/floor_" + i + ".csv");
            for(int j = 0; j < 2; j++){
                csv.append(LocalDateTime.now().toLocalTime().plusSeconds((j+1)*10).toString());
                csv.append(",");
                csv.append(String.valueOf(i));
                csv.append(",");
                csv.append("UP");
                csv.append(",");
                csv.append("2");
                csv.append("\n");
            }
            csv.flush();
            csv.close();
        }
    }
}
