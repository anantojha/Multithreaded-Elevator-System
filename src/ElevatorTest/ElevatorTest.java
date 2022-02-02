package ElevatorTest;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ElevatorTest {


    // Test input csv generation
    @Test
    public void testInputCsvGeneration() throws IOException {
        File TestFolder = new File("CSV/TestFloorCSV");

        // create test csv folder if one doesn't exist
        if(!TestFolder.exists()){
            TestFolder.createNewFile();
        } else {
            // if folder exists, delete all files from folder
            for(File f: TestFolder.listFiles())
                if (!f.isDirectory())
                    f.delete();
        }

        // generate test csv
        Elevator.Main.createFloorCSV(1, "TestFloorCSV");
        File file = new File("CSV/TestFloorCSV/floor_1.csv");

        // assert file exists and is not a directory
        assertTrue(file.exists());
        assertFalse(file.isDirectory());

        // delete after when test is complete
        // file.delete();
    }



}