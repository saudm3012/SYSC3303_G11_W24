import java.io.IOException;

public class TestElevatorDataByte {
    public static void main(String[] arg) throws IOException {
        ElevatorData data = new ElevatorData();
        ElevatorDataPacket pckobj = new ElevatorDataPacket();
        byte[] packet;
        packet = pckobj.elevatordata_to_bytes(data);
        ElevatorData data2 = pckobj.bytes_to_elevatordata(packet);

        System.out.println(data.time);
        System.out.println(data2.time);

    }
}
