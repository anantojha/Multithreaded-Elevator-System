package ElevatorTest;

import Elevator.Main;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ElevatorTest {


    // Test input csv generation
    @Test
    public void testInputCsvGeneration() throws IOException {
        Main.createFloorCSV(1, "TestFloorCSV");
        File file = new File("CSV/TestFloorCSV/floor_1.csv");
        assertTrue(file.exists());
        assertFalse(file.isDirectory());
        file.delete();
    }



}