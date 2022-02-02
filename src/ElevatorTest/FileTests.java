package ElevatorTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class FileTests {

    File TestFolder;

    @Before
    public void setup(){
        TestFolder = new File("CSV/TestFloorCSV");
        // create test csv folder if one doesn't exist
        if(!TestFolder.exists()){
            TestFolder.mkdir();
        } else {
            // if folder exists, delete all files from folder
            for(File f: TestFolder.listFiles())
                if (!f.isDirectory())
                    f.delete();
        }
    }

    // Test input csv file generation
    @Test
    public void inputCsvGeneration() throws IOException{
        // generate test csv
        Elevator.Main.createFloorCSV(1, "TestFloorCSV", 1);
        File file = new File("CSV/TestFloorCSV/floor_1.csv");

        // assert file exists and is not a directory
        assertTrue(file.exists());
        assertFalse(file.isDirectory());
    }

    @After
    public void tearDown(){
        // delete after when test is complete
        for(File f: TestFolder.listFiles())
            if (!f.isDirectory())
                f.delete();
    }
}