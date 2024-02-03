import javax.lang.model.element.ElementVisitor;
import javax.lang.model.util.ElementFilter;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.time.LocalTime;

public class TestElevatorDataByte {
    public static void main(String[] arg) throws IOException {
        /**
        ElevatorData data = new ElevatorData();
        byte[] packet;
        packet = data.elevatordata_to_bytes();
        ElevatorData data_2 = new ElevatorData();
        data_2.bytes_to_elevatordata(packet);

        System.out.println(data.time);
        System.out.println(data_2.time);
        System.out.println(data);
        System.out.println(data_2);
        */
        DataPacket data = new DataPacket("14:05:15.0","2" , "Up", "4");
        InputReader datafile = new InputReader("data.txt");
        DataPacket data2 = datafile.getNextPacket();
        System.out.println(data);
        System.out.println(data2);
        System.out.println(data.equals(data2));
    }
}
