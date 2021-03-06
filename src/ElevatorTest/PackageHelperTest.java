package ElevatorTest;

import Elevator.Global.PacketHelper;
import Elevator.FloorSubsystem.Request;
import Elevator.FloorSubsystem.Floor;
import Elevator.Enums.Direction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Before;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/*
 * PackageHelperTest tests the PackageHelper.java methods that are used in many files of our system for conversion.
 * The test will create an input file with 1 request and use PackageHelper methods to convert it to byte array or Request format.
 */
public class PackageHelperTest {
	Request request;
	byte[] task;
	DatagramPacket job;
	String expected;
	File TestFolder;
	
	@Before
	/*
	 * setup() performs necessary setup to perform test case. Creates a test folder for input file to be stored in, input file with 1 request,
	 * and reads the input file to get expected output from conversions.
	 * 
	 * Input: None
	 * Output: None
	 */
    public void setup() throws IOException {
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
        
        //Generate CSV file with 1 request
        Floor.createFloorCSV(10, "TestFloorCSV", 1);
		File file = new File("CSV/TestFloorCSV/floor.csv");
		
		//Read request in CSV file
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
            	String[] requestContents = line.split(",");
                for(Direction d: Direction.values()){
                    if(d.toString().equals(requestContents[2])){
                        LocalDate date = LocalDateTime.now().toLocalDate();
                        //Put request into Request format
                        request = new Request(LocalDateTime.parse(date.toString() + "T" + requestContents[0]), Integer.parseInt(requestContents[1]), 
                        		d, Integer.parseInt(requestContents[3]), requestContents[4]);
                        expected = request.toString();
                        System.out.println("Generated Request: " + expected);
                    }
                }
            }
		}
    }
	
	@Test
	/*
	 * changeFormat() tests all PackageHelper methods.
	 * First converts request into byte array and stores in DatagramPacket for next tests.
	 * Then converts byte array previously created and converts to Request format.
	 * Finally converts DatagramPacket previously created and converts to Request format.
	 * Checks that all conversions are working properly.
	 * 
	 * Input: None
	 * Output: None
	 */
	public void changeFormat(){
		//Test building request packet from request format
		task = PacketHelper.buildRequestPacket(request);
		System.out.println("Request Converted to Bytes: " + Arrays.toString(task));
		job = new DatagramPacket(task, task.length);
		
		
		//Test converting byte array to request format
		request = PacketHelper.convertBytesToRequest(task);
		System.out.println("Converted Bytes to Request: " + request.toString());
		Assert.assertEquals(request.toString(), expected);
		
		
		//Test converting DatagramPacket to request format
		request = PacketHelper.convertPacketToRequest(job);
		System.out.println("Convert Datagram Packet to Request: " + request.toString());
		Assert.assertEquals(request.toString(), expected);
	}

	@After
	/*
     * tearDown() cleans up after testing has been completed. 
     * Removes all created files made for the test.
     * 
     * Input: None
     * Output: None
     */
	public void tearDown() {
		//delete files made for PackageHelperTest.java
		for (File f: TestFolder.listFiles()) {
			if (!f.isDirectory()) {
				f.delete();
			}
		}
	}
}
