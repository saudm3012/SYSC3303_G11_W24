
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static org.junit.jupiter.api.Assertions.*;

class FloorSocketTest {
    private DatagramPacket receivePacket;
    private DatagramSocket sendReceiveSocket;
    private FloorSubsystem sub;
    @Test
    void testSend() throws IOException {

        sendReceiveSocket = new DatagramSocket();
        sub = new FloorSubsystem();
        DataPacket data = new DataPacket("14:05:15.0","2" , "Up", "4"); //makes new packet with values
        sub.socket.start();
        sub.socket.send(data);

        //receiving data
        DataPacket receiveData = new DataPacket();
        byte receiveDataBytes[] = new byte[1024];
        receivePacket = new DatagramPacket(receiveDataBytes, receiveDataBytes.length);
        sendReceiveSocket.receive(receivePacket);
        receiveData.bytesToDataPacket(receiveDataBytes);
        //perform assert equals with original DataPAcket
        assertTrue(data.equals(receiveData.toString()));
    }
}