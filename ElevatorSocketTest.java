
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Elevator Socket test
 * Will open a socket and send data through it using Elevator ProcessData() method
 * Then will receive the packet and make sure the values are equal
 * @Author Ali Nadim
 */

class ElevatorSocketTest {
    private DatagramPacket receivePacket;
    private DatagramSocket sendReceiveSocket,receiveSocket;
    private Elevator sub;

    private DataPacket data;


    /**
     * Setup method
     * Creates new data with same value as first entry in provided data.txt file
     * @throws IOException
     */
    @BeforeEach
    void setUp() throws IOException {
        receiveSocket = new DatagramSocket(5000);
        sendReceiveSocket = new DatagramSocket();

        sub = new Elevator();
        data = new DataPacket("14:05:15.0","2" , "Up", "4"); //makes new packet with values

    }

    /**
     * Test method
     * open a socket and send data through it using Elevator ProcessData() method
     *  * Then will receive the packet and make sure the values are equal
     * @throws IOException
     */
    @Test
    void testSend() throws IOException {
        sub.processData(data);
        //receiving data
        DataPacket receiveData = new DataPacket();
        byte receiveDataBytes[] = new byte[1024];
        receivePacket = new DatagramPacket(receiveDataBytes, receiveDataBytes.length);
        receiveSocket.receive(receivePacket);
        receiveData.bytesToDataPacket(receiveDataBytes);
        assertTrue(data.equals(receiveData));
    }
}