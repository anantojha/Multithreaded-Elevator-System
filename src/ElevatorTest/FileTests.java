package ElevatorTest;

import Elevator.FloorSubsystem.Floor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;


/*
 * FileTests tests that the input files for the system are properly generated.
 * The test creates a test folder where the input files will be created and checked.
 */
public class FileTests {

    File TestFolder;

    @Before
    /*
     * setup() performs necessary setup to perform test case. Creates test folder where input file will be temporarily created.
     * If folder already exists then it will delete all files in the folder.
     * 
     * Input: None
     * Output: None
     */
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

    @Test
    /*
     * inputCsvGeneration() generates the input file for the system, checks the input file was properly made and
     * outputs generated data to console. 
     * 
     * Input: None
     * Output: None
     */
    public void inputCsvGeneration() throws IOException{
        // generate test csv for 22 floors and a total of 22 requests
        Floor.createFloorCSV(22, "TestFloorCSV", 22);
        File file = new File("CSV/TestFloorCSV/floor.csv");

        // assert file exists and is not a directory
        assertTrue(file.exists());
        assertFalse(file.isDirectory());
        System.out.println("Generated CSV Data: ");

        // print CSV data
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    @After
    /*
     * tearDown() cleans up after testing has been completed. 
     * Removes all created files made for the test.
     * 
     * Input: None
     * Output: None
     */
    public void tearDown(){
        // delete after when test is complete
        for(File f: TestFolder.listFiles())
            if (!f.isDirectory())
                f.delete();
    }
}