package Elevator.Global;

import Elevator.Enums.Direction;
import Elevator.Enums.SubSystemMapping;
import Elevator.FloorSubsystem.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketHelper {
    public static byte[] buildRequestPacket(Request request){
        byte[] packet;
        ByteArrayOutputStream buildPacket = new ByteArrayOutputStream();

        // Packet Format:
        // 18:20:59.090,1,UP,4
        // [dash-delimiter][requestFloor][dash-delimiter][Time][Dash-Delimiter][Direction][Dash-Delimiter][SourceFloor][Dash-Delimiter][DestinationFloor][Dash-Delimiter][FaultFlag][Dash-Delimiter]
        try {
            buildPacket.write('_');
            buildPacket.write(SubSystemMapping.FLOOR.subSystemId());
            buildPacket.write('_');
            buildPacket.write(request.getTime().toString().getBytes());
            buildPacket.write('_');
            buildPacket.write(request.getDirection().getDirectionId());
            buildPacket.write('_');
            buildPacket.write(request.getSourceFloor());
            buildPacket.write('_');
            buildPacket.write(request.getDestinationFloor());
            buildPacket.write('_');
            buildPacket.write(request.getFaultByte());
            buildPacket.write('_');
        } catch (IOException e) {
            System.exit(1);
        }

        packet = buildPacket.toByteArray();
        return packet;
    }

    public static Request convertBytesToRequest(byte[] data){

        List<Integer> delimitIndices = new ArrayList<>();
        for(int i = 0; i < data.length; i++){
            if(data[i] == 95){
                delimitIndices.add(i);
            }
        }

        byte[] subSystemBytes = Arrays.copyOfRange(data, delimitIndices.get(0)+1, delimitIndices.get(1));
        byte[] timeBytes = Arrays.copyOfRange(data, delimitIndices.get(1)+1, delimitIndices.get(2));
        byte[] directionBytes = Arrays.copyOfRange(data, delimitIndices.get(2)+1, delimitIndices.get(3));
        byte[] sourceFloorBytes = Arrays.copyOfRange(data, delimitIndices.get(3)+1, delimitIndices.get(4));
        byte[] destinationFloorBytes = Arrays.copyOfRange(data, delimitIndices.get(4)+1, delimitIndices.get(5));
        byte[] faultBytes = Arrays.copyOfRange(data, delimitIndices.get(5)+1, delimitIndices.get(6));

        Request request = new Request(LocalDateTime.parse(new String(timeBytes)), sourceFloorBytes[0], Direction.getDirectionFromId(directionBytes[0]), destinationFloorBytes[0], faultBytes[0] == 1 ? "f":"n");
        return request;
    }

    public static Request convertPacketToRequest(DatagramPacket packet){
        // [95, 1, 95, 50, 48, 50, 50, 45, 48, 51, 45, 50, 55, 84, 48, 50, 58, 51, 51, 58, 52, 57, 46, 50, 55, 53, 95, 1, 95, 3, 95, 4, 95, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        byte[] data = packet.getData();
        List<Integer> delimitIndices = new ArrayList<>();
        for(int i = 0; i < packet.getLength(); i++){
            if(data[i] == 95){
                delimitIndices.add(i);
            }
        }

        byte[] subSystemBytes = Arrays.copyOfRange(data, delimitIndices.get(0)+1, delimitIndices.get(1));
        byte[] timeBytes = Arrays.copyOfRange(data, delimitIndices.get(1)+1, delimitIndices.get(2));
        byte[] directionBytes = Arrays.copyOfRange(data, delimitIndices.get(2)+1, delimitIndices.get(3));
        byte[] sourceFloorBytes = Arrays.copyOfRange(data, delimitIndices.get(3)+1, delimitIndices.get(4));
        byte[] destinationFloorBytes = Arrays.copyOfRange(data, delimitIndices.get(4)+1, delimitIndices.get(5));
        byte[] faultBytes = Arrays.copyOfRange(data, delimitIndices.get(5)+1, delimitIndices.get(6));

        Request request = new Request(LocalDateTime.parse(new String(timeBytes)), sourceFloorBytes[0], Direction.getDirectionFromId(directionBytes[0]), destinationFloorBytes[0], faultBytes[0] == 1 ? "f":"n");
        return request;
    }
}
