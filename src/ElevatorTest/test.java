package ElevatorTest;

import Elevator.Global.PacketHelper;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class test {

    @Test
    public void inputCsvGeneration() throws IOException {

        byte[] data = {95, 1, 95, 50, 48, 50, 50, 45, 48, 51, 45, 50, 55, 84, 48, 50, 58, 51, 51, 58, 52, 57, 46, 50, 55, 53, 95, 1, 95, 3, 95, 4, 95, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        InetAddress localHostVar = InetAddress.getLocalHost();;
        DatagramPacket packet = new DatagramPacket(data, data.length, localHostVar, 2505);
        PacketHelper.convertPacketToRequest(packet);
    }
}
